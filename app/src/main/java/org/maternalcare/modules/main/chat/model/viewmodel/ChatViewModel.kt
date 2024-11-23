package org.maternalcare.modules.main.chat.model.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import org.maternalcare.modules.main.chat.model.dto.ChatDto
import org.maternalcare.modules.main.chat.model.dto.MessageDto
import org.maternalcare.modules.main.chat.model.dto.UserChatDto
import org.maternalcare.modules.main.chat.service.ChatService
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatService: ChatService,
): ViewModel() {
    fun fetchUsers(userId: ObjectId): Flow<List<UserChatDto>> {
        return chatService.fetchUsers(userId)
    }

    suspend fun findOneChatOrCreate(sender: UserDto, receiver: UserDto): Result<ChatDto> {
        return chatService.findOneChatOrCreate(sender, receiver)
    }

    fun fetchDirectMessages(sender: UserDto, receiver: UserDto): Flow<List<MessageDto>> {
        return chatService.fetchDirectMessages(sender, receiver)
    }

    suspend fun sendMessage(sender: UserDto, receiver: UserDto, content: String): Result<MessageDto> {
        return chatService.message(sender, receiver, content)
    }
}