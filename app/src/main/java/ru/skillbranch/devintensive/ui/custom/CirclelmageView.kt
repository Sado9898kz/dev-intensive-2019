package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import ru.skillbranch.devintensive.R
import kotlin.math.min

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {

        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2

        private var textPaint: TextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        private var text: String? = null

        private var textSize: Int = 16
        private var textColor = DEFAULT_BORDER_COLOR

    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = convertDp2Px(DEFAULT_BORDER_WIDTH)

    // Rect with float values
    private val drawableRect = RectF()
    private val borderRect = RectF()

    // Matrix for transformations
    private val shaderMatrix = Matrix()

    // Paint for drawing setting
    private val bitmapPaint = Paint()
    private val borderPaint = Paint()
    private val circleBackgroundPaint = Paint()

    // bitmap
    private var bitmap: Bitmap? = null
    private var bitmapShader: BitmapShader? = null
    private var bitmapWidth: Int = 0
    private var bitmapHeight: Int = 0

    // radius
    private var drawableRadius: Float = 0f
    private var borderRadius: Float = 0f

    // color filters
    private var colorFilter: ColorFilter? = null

    // checks
    private var isReady: Boolean = false
    private var isSetupPending: Boolean = false
    private var isBorderOverlay = false

    init {

        if (attrs != null) {
            val c = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)

            setIntBorderColor(c.getColor(R.styleable.CircleImageView_cv_borderColor, borderColor))
            borderWidth = c.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
            Log.d("M_Init_CircleImageView", "init")

            text = c.getString(R.styleable.CircleImageView_cv_text)

            setTextSize(c.getDimensionPixelSize(R.styleable.CircleImageView_cv_textSize, textSize))
            setIntTextColor(c.getColor(R.styleable.CircleImageView_cv_textColor, textColor))
            c.recycle()

            super.setScaleType(ScaleType.CENTER_CROP)
            isReady = true

            if (isSetupPending) {
                setup()
                isSetupPending = false
            }

        }

    }

    @Dimension
    fun getBorderWidth() = convertPx2Dp(borderWidth)

    fun setBorderWidth(@Dimension dp: Int) {
        val newWidth = convertDp2Px(dp)
        if (newWidth != borderWidth) {
            borderWidth = newWidth
            setup()
        }
    }

    fun getBorderColor() = borderColor

    fun setBorderColor(hex: String) = setIntBorderColor(Color.parseColor(hex))

    fun setBorderColor(@ColorRes colorId: Int) {
        setIntBorderColor(resources.getColor(colorId, context.theme))
    }


    private fun setIntBorderColor(@ColorInt colorValue: Int) {

        if (colorValue != borderColor) {
            borderColor = colorValue
            borderPaint.color = borderColor
            invalidate()
        }

    }

    fun setText(newText: String?) {
        text = newText
        setup()
    }

    override fun onDraw(canvas: Canvas) {

        bitmap ?: return

        if (borderWidth > 0) canvas.drawCircle(borderRect.centerX(), borderRect.centerY(), borderRadius, borderPaint)

        canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRadius, bitmapPaint)

        if (text != null) {
            val centerX = Math.round(canvas.width * 0.5f)
            val centerY = Math.round(canvas.height * 0.5f)
            val textWidth = textPaint.measureText(text) * 0.5f
            val textBaseLineHeight = textPaint.fontMetrics.ascent * -0.4f

            canvas.drawText(text.toString(), centerX - textWidth, centerY + textBaseLineHeight, textPaint)

        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setup()
    }


    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        setup()
    }


    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        initializeBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        if (drawable is ColorDrawable) {
            val image = Bitmap.createBitmap(250, 250, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)

            canvas.drawColor(drawable.color)
            setImageBitmap(image)

        } else {
            super.setImageDrawable(drawable)
            initializeBitmap()
        }
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        initializeBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        initializeBitmap()
    }


    override fun setColorFilter(cf: ColorFilter) {
        if (cf === colorFilter) return

        colorFilter = cf
        applyColorFilter()
        invalidate()
    }

    override fun getColorFilter(): ColorFilter? = colorFilter

    private fun applyColorFilter() {
        bitmapPaint.colorFilter = colorFilter
    }


    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {

        with(drawable) {

            return when (this) {
                null -> null
                is BitmapDrawable -> bitmap
                else -> {
                    val width = if (this is ColorDrawable) 1 else intrinsicWidth
                    val height = if (this is ColorDrawable) 1 else intrinsicHeight
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)

                    setBounds(0, 0, canvas.width, canvas.height)
                    draw(canvas)
                    bitmap
                }
            }
        }
    }

    private fun initializeBitmap() {
        bitmap = drawable.toBitmap()
        setup()
    }

    private fun setup() {

        if (!isReady) {
            isSetupPending = true
            return
        }

        if (width == 0 && height == 0) return

        if (bitmap == null) {
            invalidate()
            return
        }

        bitmapShader = BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        bitmapPaint.isAntiAlias = true
        bitmapPaint.shader = bitmapShader

        with(borderPaint) {
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = borderColor
            strokeWidth = borderWidth.toFloat()
        }

        with(circleBackgroundPaint) {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = Color.TRANSPARENT
        }

        bitmapHeight = bitmap!!.height
        bitmapWidth = bitmap!!.width

        borderRect.set(calculateBounds())
        borderRadius = min((borderRect.height() - borderWidth) / 2.0f, (borderRect.width() - borderWidth) / 2.0f)

        drawableRect.set(borderRect)
        if (!isBorderOverlay && borderWidth > 0) {
            drawableRect.inset(borderWidth - 1.0f, borderWidth - 1.0f)
        }

        drawableRadius = Math.min(drawableRect.height() / 2.0f, drawableRect.width() / 2.0f)

        applyColorFilter()
        updateShaderMatrix()
        invalidate()
    }

    private fun calculateBounds(): RectF {

        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom
        val sideLength = min(availableWidth, availableHeight)
        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }

    private fun updateShaderMatrix() {

        val scale: Float
        var dx = 0f
        var dy = 0f

        shaderMatrix.set(null)

        if (bitmapWidth * drawableRect.height() > drawableRect.width() * bitmapHeight) {
            scale = drawableRect.height() / bitmapHeight.toFloat()
            dx = (drawableRect.width() - bitmapWidth * scale) * 0.5f
        } else {
            scale = drawableRect.width() / bitmapWidth.toFloat()
            dy = (drawableRect.height() - bitmapHeight * scale) * 0.5f
        }

        shaderMatrix.setScale(scale, scale)
        shaderMatrix.postTranslate((dx + 0.5f).toInt() + drawableRect.left, (dy + 0.5f).toInt() + drawableRect.top)
        bitmapShader!!.setLocalMatrix(shaderMatrix)
    }

    fun setTextSize(size: Int) {
        textSize = size
        textPaint.setTextSize(textSize * resources.displayMetrics.scaledDensity)
    }

    fun setTextColor(@ColorRes colorId: Int) = setIntTextColor(resources.getColor(colorId, context.theme))

    fun setTextColor(hexColor: String) = setIntTextColor(Color.parseColor(hexColor))

    private fun setIntTextColor(@ColorInt colorValue: Int) {
        textColor = colorValue
        textPaint.setColor(textColor)
    }

    private fun convertDp2Px(dp: Int): Int = (dp * resources.displayMetrics.density + 0.5f).toInt()
    private fun convertPx2Dp(px: Int): Int = (px / resources.displayMetrics.density + 0.5f).toInt()
}