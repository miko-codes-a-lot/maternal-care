package org.maternalcare.modules.main.chat.model.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import org.maternalcare.modules.main.chat.model.dto.UserChatDto
import org.maternalcare.modules.main.chat.service.ChatService
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatService: ChatService,
): ViewModel() {
    fun fetchUsers(userId: ObjectId): Flow<List<UserChatDto>> {
        return chatService.fetchUsers(userId)
    }
}