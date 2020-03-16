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
        TODO("Not yet implemented")
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        return when {
            firstName.isNullOrBlank() && lastName.isNullOrBlank() -> null
            else -> "${firstName?.first()?.toUpperCase().toString() ?: ""}${lastName?.first()?.toUpperCase() ?: ""}"
        }
    }
}