package org.maternalcare.modules.main.chat.service

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import org.maternalcare.modules.main.chat.mapper.toDTO
import org.maternalcare.modules.main.chat.model.dto.UserChatDto
import org.maternalcare.modules.main.chat.model.entity.Chat
import org.maternalcare.modules.main.user.model.entity.User
import org.maternalcare.modules.main.user.model.mapper.toDTO
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class ChatService @Inject constructor(private val realm: Realm) {
    fun fetchUsers(userId: ObjectId): List<UserChatDto> {
        return realm.query<User>("createdById == $0", userId)
            .sort("firstName", Sort.DESCENDING)
            .find()
            .map { user ->
                val chat = realm
                    .query<Chat>("user1Id == $0 AND user2Id == $1", userId, user._id)
                    .find()
                    .firstOrNull()
                    ?: Chat().apply {
                        user1Id = userId
                        user2Id = user._id
                    }

                UserChatDto(
                    user.toDTO(),
                    chat.toDTO()
                )
            }
    }
}