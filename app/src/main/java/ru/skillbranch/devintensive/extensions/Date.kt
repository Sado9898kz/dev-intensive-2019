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

fun Date.shortFormat(): String {
    val pattern = if (this.isSameDay(Date())) "HH:mm" else "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.isSameDay(date: Date): Boolean {
    val day1 = this.time / DAY
    val day2 = date.time / DAY
    return day1 == day2
}

fun Date.add(value: Int, units: TimeUnits): Date {

    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {

    var message = " "
    val time: Long = this.time - date.time

    when (time) {
        in -1 * SECOND..0 -> message = "только что"
        in -45 * SECOND..-1 * SECOND -> message = "несколько секунд назад"
        in 1 * SECOND..45 * SECOND -> message = "через несколько секунд"
        in -75 * SECOND..-45 * SECOND -> message = "минуту назад"
        in 45 * SECOND..75 * SECOND -> message = "через минуту"
        in -4 * MINUTE..-75 * SECOND -> message = "${(time * -1) / MINUTE} минуты назад"
        in 75 * SECOND..4 * MINUTE -> message = "через ${time / MINUTE} минуты"
        in -45 * MINUTE..-5 * MINUTE -> message = "${(time * -1) / MINUTE} минут назад"
        in 5 * MINUTE..45 * MINUTE -> message = "через ${time / MINUTE} минут"
        in -75 * MINUTE..-45 * MINUTE -> message = "час назад"
        in 45 * MINUTE..75 * MINUTE -> message = "через час"
        in -4 * HOUR..-75 * MINUTE -> message = "${(time * -1) / HOUR} часа назад"
        in 75 * MINUTE..4 * HOUR -> message = "через ${time / HOUR} часа"
        in -22 * HOUR..-5 * HOUR -> message = "${(time * -1) / HOUR} часов назад"
        in 5 * HOUR..22 * HOUR -> message = "через ${time / HOUR} часов"
        in -26 * HOUR..-22 * HOUR -> message = "день назад"
        in 22 * HOUR..26 * HOUR -> message = "через день"
        in -360 * DAY..-26 * HOUR -> message = "${(time * -1) / DAY} дней назад"
        in 26 * HOUR..360 * DAY -> message = "через ${time / DAY} дней"
        in -10000 * DAY..-360 * DAY -> message = "более года назад"
        in 360 * DAY..10000 * DAY -> message = "более чем через год"
    }

    return message
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}