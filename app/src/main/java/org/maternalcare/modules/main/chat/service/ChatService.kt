package org.maternalcare.modules.main.chat.service

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.maternalcare.modules.main.chat.mapper.toDTO
import org.maternalcare.modules.main.chat.mapper.toDto
import org.maternalcare.modules.main.chat.mapper.toEntity
import org.maternalcare.modules.main.chat.model.dto.ChatDto
import org.maternalcare.modules.main.chat.model.dto.MessageDto
import org.maternalcare.modules.main.chat.model.dto.UserChatDto
import org.maternalcare.modules.main.chat.model.entity.Chat
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.entity.User
import org.maternalcare.modules.main.user.model.mapper.toDTO
import org.maternalcare.shared.ext.toInstantK
import org.maternalcare.shared.ext.toObjectId
import org.mongodb.kbson.ObjectId
import java.security.MessageDigest
import javax.inject.Inject

class ChatService @Inject constructor(private val realm: Realm) {
    private fun generateId(input: String): ObjectId {
        // Create an MD5 hash of the input string
        val md = MessageDigest.getInstance("MD5")
        val fullHash = md.digest(input.toByteArray())

        // Truncate the hash to 12 bytes (24 hexadecimal characters)
        val truncatedHash = fullHash.copyOfRange(0, 12)

        // Convert the truncated hash to a hexadecimal string
        val hexString = truncatedHash.joinToString("") { "%02x".format(it) }

        // Construct and return the ObjectId
        return ObjectId(hexString)
    }

    fun fetchUsers(userId: ObjectId): Flow<List<UserChatDto>> {
        return realm.query<User>("createdById == $0", userId)
            .sort("firstName", Sort.DESCENDING)
            .find()
            .asFlow()
            .map { resultsChange ->
                resultsChange.list.map { user ->
                    val chatId = generateId("${userId.toHexString()}${user._id.toHexString()}")
                    val chat = realm.query<Chat>(
                        "_id == $0",
                        chatId
                    ).find().firstOrNull()

                    if (user.firstName == "Angel") {
                        Log.d("micool", "${userId.toHexString()} : ${user._id.toHexString()} : ${chatId.toHexString()}")
                    }

                    UserChatDto(
                        user.toDTO(),
                        (chat ?: Chat().apply {
                            _id = chatId
                            user1Id = userId
                            user2Id = user._id
                        }).toDTO(),
                        chat?.updatedAt?.toInstantK()
                    )
                }.sortedByDescending { it.updatedAt }
            }
    }

    private fun findOneChat(id: ObjectId): ChatDto {
        val chat = realm.query<Chat>("_id == $0", id)
            .find()
            .firstOrNull()

        if (chat == null) throw Exception("Chat not found")

        return chat.toDTO()
    }

    /**
     * @param sender - if sender is residence then chat becomes isRead = false
     */
    suspend fun message(sender: UserDto, messageDto: MessageDto): Result<MessageDto> {
        return try {
            val chatDto = findOneChat(messageDto.chatId.toObjectId())

            realm.write {
                val message = copyToRealm(messageDto.toEntity(), updatePolicy = UpdatePolicy.ALL)
                if (sender.isResidence) {
                    val chat = chatDto.toEntity().apply { isRead = false }
                    copyToRealm(chat, updatePolicy = UpdatePolicy.ALL)
                }
                Result.success(message.toDto())
            }
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}