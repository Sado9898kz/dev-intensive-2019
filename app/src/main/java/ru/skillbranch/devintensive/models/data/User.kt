package ru.skillbranch.devintensive.models.data

import ru.skillbranch.devintensive.extensions.humanizeDiff
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User(
        val id: String,
        var firstName: String?,
        var lastName: String?,
        var avatar: String?,
        var rating: Int = 0,
        var respect: Int = 0,
        val lastVisit: Date? = null,
        val isOnline: Boolean = false
) {
    fun toUserItem(): UserItem {
        val lastActivity = when {
            lastVisit == null -> "Еще ни разу не заходил"
            isOnline -> "online"
            else -> "Последний раз был ${lastVisit.humanizeDiff()}"
        }

        return UserItem(
                id,
                "${firstName.orEmpty()} ${lastName.orEmpty()}",
                Utils.toInitials(firstName, lastName),
                avatar,
                lastActivity,
                false,
                isOnline
        )
    }

    constructor(id: String, firstName: String?, lastName: String?) : this(
            id = id,
            firstName = firstName,
            lastName = lastName,
            avatar = null
    )

    constructor(id: String) : this(id, firstName = "John", lastName = "Doe")

    companion object Factory {
        private var lastId: Int = -1
        fun makeUser(fullName: String?): User {
            lastId++

            val (firstName, lastName) = Utils.parseFullName(fullName)

            return User(id = "$lastId", firstName = firstName, lastName = lastName)
        }

        fun Builder(
                id: String,
                firstName: String?,
                lastName: String?,
                avatar: String?,
                rating: Int = 0,
                respect: Int = 0,
                lastVisit: Date? = null,
                isOnline: Boolean = false
        ): User {
            lastId++

            return User(
                    id = "$lastId",
                    firstName = firstName,
                    lastName = lastName,
                    avatar = null,
                    rating = 29,
                    respect = 300,
                    lastVisit = Date(),
                    isOnline = true
            )
        }
    }

}