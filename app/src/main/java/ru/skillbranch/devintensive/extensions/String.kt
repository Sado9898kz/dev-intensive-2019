package ru.skillbranch.devintensive.extensions


fun String.truncate(value: Int = 16): String {

    val newStr = this.trim()

    return when {
        newStr.length > value -> newStr.substring(0, value).plus("...")
        else -> newStr
    }
}

fun String.stripHtml(): String =
    this.replace("[ ]+".toRegex(), " ")
        .replace("<[^>]*>".toRegex(), "")
        .replace("&[^>]*;".toRegex(), "")