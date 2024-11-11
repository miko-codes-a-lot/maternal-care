package org.maternalcare.modules.main.message.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow
import org.maternalcare.modules.main.message.model.dto.MessageDto
import org.maternalcare.modules.main.message.model.entity.Message
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

    fun fetchMessagesAsync(senderId: ObjectId, receiverId: ObjectId): Flow<ResultsChange<Message>> {
        return messageService.fetchAsFlow(senderId, receiverId)

    }

    suspend fun sendMessage(messageDto: MessageDto): Result<MessageDto> {
        messageDto.isRead = false
        return messageService.send(messageDto)
    }

    fun fetchLatestMessageFlow(senderId: ObjectId, receiverId: ObjectId): Flow<MessageDto?> {
        return messageService.fetchLatestMessage(senderId, receiverId)
    }

    suspend fun markMessageAsRead(messageDto: MessageDto) {
        if (!messageDto.isRead) {
            messageDto.isRead = true
            messageService.updateMessageReadStatus(messageDto)
        }
    }
}