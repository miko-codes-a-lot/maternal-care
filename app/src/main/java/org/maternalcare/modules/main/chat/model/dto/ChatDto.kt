package org.maternalcare.modules.main.chat.model.dto

import kotlinx.datetime.Instant

data class ChatDto(
    val id: String,
    val user1Id: String,
    val user2Id: String,
    val lastMessage: String,
    val isRead: Boolean,
    val updatedAt: Instant,
)