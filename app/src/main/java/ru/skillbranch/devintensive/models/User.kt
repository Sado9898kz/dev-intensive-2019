package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User(
    val id: String,
    var firstName: String?,
    var lastName: String?,
    var avatar: String?,
    var rating: Int = 0,
    var respect: Int = 0,
    val lastVisit: Date? = Date(),
    val isOnline: Boolean = false
) {
    constructor(id: String, firstName: String?, lastName: String?)
            : this(id = id, firstName = firstName, lastName = lastName, avatar = null)

    constructor(id: String) : this(id, firstName = "John", lastName = "Doe")

    init {
        println(
            "It`s Alive!!! \n" +
                    "${if (lastName === "Doe") "His name is $firstName $lastName" else "And his name is $firstName $lastName"}\n"
        )
    }

    companion object Factory {
        private var lastId: Int = -1
        fun makeUser(fullName: String?): User {
            lastId++

            val (firstName, lastName) = Utils.parseFullName(fullName)
            return User(id = "$lastId", firstName = firstName, lastName = lastName)
        }

        fun Builder(
            id: String = "$lastId",
            firstName: String?,
            lastName: String?,
            avatar: String?,
            rating: Int = 0,
            respect: Int = 0,
            lastVisit: Date? = Date(),
            isOnline: Boolean = false
        ) = User(
            id = id,
            firstName = firstName,
            lastName = lastName,
            avatar = avatar,
            rating = rating,
            respect = respect,
            lastVisit = lastVisit,
            isOnline = isOnline
        )
    }
}