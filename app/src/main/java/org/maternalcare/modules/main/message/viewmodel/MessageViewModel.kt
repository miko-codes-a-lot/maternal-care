package org.maternalcare.modules.main.message.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.maternalcare.modules.main.message.model.dto.MessageDto
import org.maternalcare.modules.main.message.service.MessageService
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.service.UserService
import org.maternalcare.shared.ext.toObjectId
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageService: MessageService,
    private val userService: UserService
): ViewModel() {
    fun getResidences(userId: String): List<UserDto> {
        return userService.fetch(isResidence = true, userId = userId.toObjectId())
    }

    fun fetchMessages(senderId: ObjectId, receiverId: ObjectId): List<MessageDto> {
        return messageService.fetch(senderId, receiverId)
    }

    suspend fun sendMessage(messageDto: MessageDto): Result<MessageDto> {
        return messageService.send(messageDto)
    }
}