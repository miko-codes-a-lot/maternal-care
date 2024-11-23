package org.maternalcare.modules.main.chat.model.dto

import kotlinx.datetime.Instant

data class MessageDto(
    val id: String,
    val chatId: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val createdAt: Instant
)
