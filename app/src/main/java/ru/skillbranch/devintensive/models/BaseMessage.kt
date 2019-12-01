package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.User
import java.util.*

abstract class BaseMessage(
        val id: String,
        val from: User?,
        val chat: Chat,
        val isIncoming: Boolean = false,
        val date: Date = Date()
) {

    abstract fun formatMessage(): String

    companion object AbstractFactory {

        var lastId = -1

        fun makeMessage(
                from: User?,
                chat: Chat,
                date: Date = Date(),
                type: String = "text",
                payload: Any?,
                isIncoming: Boolean = false
        ): BaseMessage {
            lastId++

            return when (type) {
                "image" -> ImageMessege("$lastId", from, chat, date = date, image = payload as String,isIncoming = isIncoming)
                else -> TextMessage("$lastId", from, chat, date = date, text = payload as String,isIncoming = isIncoming)

            }
        }
    }
}