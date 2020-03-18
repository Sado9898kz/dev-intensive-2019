package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.split(" ")

        return when {
            parts?.getOrNull(0).isNullOrBlank() -> null to null
            else -> parts?.getOrNull(0) to parts?.getOrNull(1)
        }
    }

    fun transliteration(payload: String, divider: String = " "): String {
        fun convertSymbol(char: CharSequence): CharSequence {
            return when (char) {
                "а" -> "a"
                "б" -> "b"
                "в" -> "v"
                "г" -> "g"
                "д" -> "d"
                "е", "ё", "э" -> "e"
                "ж" -> "zh"
                "з" -> "z"
                "и", "й", "ы" -> "i"
                "к" -> "k"
                "л" -> "l"
                "м" -> "m"
                "н" -> "n"
                "о" -> "o"
                "п" -> "p"
                "р" -> "r"
                "с" -> "s"
                "т" -> "t"
                "у" -> "u"
                "ф" -> "f"
                "х" -> "h"
                "ц" -> "c"
                "ч" -> "ch"
                "ш", "щ" -> "sh"
                "ю" -> "yu"
                "я" -> "ya"
                "ь", "ъ" -> ""
                " " -> divider
                else -> char
            }
        }

        return payload.replace(Regex("[a-я]|[ ]+")) { convertSymbol(it.value) }
            .replace(Regex("[А-я]")) { convertSymbol(it.value.toLowerCase()).toString().toUpperCase() }
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        return when {
            firstName.isNullOrBlank() && lastName.isNullOrBlank() -> null
            else -> "${firstName?.first()?.toUpperCase() ?: ""}" +
                    "${lastName?.first()?.toUpperCase() ?: ""}"
        }
    }
}