package org.maternalcare.modules.main.message.model.dto

data class MessageDto(
    var id: String? = null,
    var senderId: String? = null,
    var receiverId: String? = null,
    var date: String = "",
)