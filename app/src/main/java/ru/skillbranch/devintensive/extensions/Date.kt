package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, timeUnits: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (timeUnits) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    this.time = time
    return this
}


fun Date.humanizeDiff(date: Date = Date()): String =
    when (val time: Long = this.time - date.time) {
        in -1 * SECOND..0 -> "только что"
        in -45 * SECOND..-1 * SECOND -> "несколько секунд назад"
        in 1 * SECOND..45 * SECOND -> "через несколько секунд"
        in -75 * SECOND..-45 * SECOND -> "минуту назад"
        in 45 * SECOND..75 * SECOND -> "через минуту"
        in -4 * MINUTE..-75 * SECOND -> "${(time * -1) / MINUTE} минуты назад"
        in 75 * SECOND..4 * MINUTE -> "через ${time / MINUTE} минуты"
        in -45 * MINUTE..-5 * MINUTE -> "${(time * -1) / MINUTE} минут назад"
        in 5 * MINUTE..45 * MINUTE -> "через ${time / MINUTE} минут"
        in -75 * MINUTE..-45 * MINUTE -> "час назад"
        in 45 * MINUTE..75 * MINUTE -> "через час"
        in -4 * HOUR..-75 * MINUTE -> "${(time * -1) / HOUR} часа назад"
        in 75 * MINUTE..4 * HOUR -> "через ${time / HOUR} часа"
        in -22 * HOUR..-5 * HOUR -> "${(time * -1) / HOUR} часов назад"
        in 5 * HOUR..22 * HOUR -> "через ${time / HOUR} часов"
        in -26 * HOUR..-22 * HOUR -> "день назад"
        in 22 * HOUR..26 * HOUR -> "через день"
        in -360 * DAY..-26 * HOUR -> "${(time * -1) / DAY} дней назад"
        in 26 * HOUR..360 * DAY -> "через ${time / DAY} дней"
        in -Int.MAX_VALUE * DAY..-360 * DAY -> "более года назад"
        in 360 * DAY..Int.MAX_VALUE * DAY -> "более чем через год"
        else -> ""
    }


enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}

fun TimeUnits.plural(value: Int): String = when (this) {
    TimeUnits.SECOND -> "$value секунду"
    TimeUnits.MINUTE -> "$value минуты"
    TimeUnits.HOUR -> "$value часов"
    TimeUnits.DAY -> "$value дня"
}