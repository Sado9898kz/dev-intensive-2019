package ru.skillbranch.devintensive.extensions

fun String.truncate(char: Int = 16): String = when {
    this[char] == ' ' -> this.trim()
    else -> this.removeRange(char, this.length).trim().plus("...")
}

fun String.stripHtml(): String =
    this.replace("[ ]+|<[^>]*>|&[^>]*;".toRegex(), " ")