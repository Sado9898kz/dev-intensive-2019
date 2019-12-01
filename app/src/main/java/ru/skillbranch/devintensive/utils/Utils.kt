package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {

        val parts: List<String>? = fullName?.split(" ")

        val firstName = if (parts?.getOrNull(0)?.trimIndent() == "") {
            null
        } else {
            parts?.getOrNull(0)
        }
        val lastName = if (parts?.getOrNull(1)?.trimIndent() == "") {
            null
        } else {
            parts?.getOrNull(1)
        }

        /**if (fullName?.trimIndent() == "") {
        fullName?=null
        }

        val parts: List<String>? = fullName?.split(" ")

        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)*/

        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = "_"): String {

        var add = ""

        fun compare(symbol: Char): String {

            val compare = when (symbol.toLowerCase()) {
                'а' -> "a"
                'б' -> "b"
                'в' -> "v"
                'г' -> "g"
                'д' -> "d"
                'е', 'ё', 'э' -> "e"
                'ж' -> "zh"
                'з' -> "z"
                'и', 'й', 'ы' -> "i"
                'к' -> "k"
                'л' -> "l"
                'м' -> "m"
                'н' -> "n"
                'о' -> "o"
                'п' -> "p"
                'р' -> "r"
                'с' -> "s"
                'т' -> "t"
                'у' -> "u"
                'ф' -> "f"
                'х' -> "h"
                'ц' -> "c"
                'ч' -> "ch"
                'ш', 'щ' -> "sh"
                'ю' -> "yu"
                'я' -> "ya"
                'ь', 'ъ' -> ""
                ' ' -> divider
                else -> "$symbol"
            }

            return compare
        }

        payload.forEach { c ->
            add += (if (c.isUpperCase()) {
                compare(c).capitalize()
            } else {
                compare(c)
            })
        }

        return add
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val name: String?

        when {
            firstName.isNullOrBlank() && lastName.isNullOrBlank() -> name = null
            firstName.isNullOrBlank() -> name = "${listOf(lastName!![0]).first().toUpperCase()}"
            lastName.isNullOrBlank() -> name = "${listOf(firstName!![0]).first().toUpperCase()}"
            else -> name = "${listOf(firstName!![0]).first().toUpperCase()}" +
                    "${listOf(lastName!![0]).first().toUpperCase()}"
        }

        return name
    }

    fun urlTest(url: CharSequence?): Boolean {

        val waringName= listOf(
            "enterprise",
            "features",
            "topics",
            "collections",
            "trending",
            "events",
            "marketplace",
            "pricing",
            "nonprofit",
            "customer-stories",
            "security",
            "login",
            "join"
        ).joinToString("|")

        val pattern = Regex("""^(https://)?(www\.)?github\.com/(?!($waringName)/?$)[\-\w]+/?$""")
        return url.isNullOrBlank() || pattern.matches(url)
    }

}