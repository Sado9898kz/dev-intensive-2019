package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTES = 60 * SECOND
const val HOUR = 60 * MINUTES
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, timeUnits: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (timeUnits) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTES
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    this.time = time
    return this
}


fun Date.humanizeDiff(date: Date = Date()): String {
    TODO("Not yet implemented")
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