package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlin.math.roundToLong

fun Activity.hideKeyboard(){
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Activity.isKeyboardOpen(): Boolean{

    val r = Rect()
    val rootView = findViewById<View>(android.R.id.content)
    rootView.getWindowVisibleDisplayFrame(r)

    val heightDiff = rootView.height - r.height()
    val marginErr = this.convertDPtoPX(50F)

    return heightDiff > marginErr
}

fun Activity.isKeyboardClosed(): Boolean{
    return this.isKeyboardOpen().not()
}

fun Activity.convertDPtoPX(dp: Float) : Long{
    val r = this.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics
    ).roundToLong()
}