package org.maternalcare.modules.main.chat.service

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import org.maternalcare.modules.main.chat.mapper.toDTO
import org.maternalcare.modules.main.chat.mapper.toDto
import org.maternalcare.modules.main.chat.mapper.toEntity
import org.maternalcare.modules.main.chat.model.dto.ChatDto
import org.maternalcare.modules.main.chat.model.dto.MessageDto
import org.maternalcare.modules.main.chat.model.dto.UserChatDto
import org.maternalcare.modules.main.chat.model.entity.Chat
import org.maternalcare.modules.main.chat.model.entity.Message
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.entity.User
import org.maternalcare.modules.main.user.model.mapper.toDTO
import org.maternalcare.shared.ext.toInstantK
import org.maternalcare.shared.ext.toObjectId
import org.mongodb.kbson.ObjectId
import java.security.MessageDigest
import javax.inject.Inject

class ChatService @Inject constructor(private val realm: Realm) {
    private fun generateChatId(input: String): ObjectId {
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

    private fun getAdminId(senderDto: UserDto, receiver: UserDto): String? {
        return if (senderDto.isAdmin) senderDto.id else receiver.id
    }

    private fun getUserId(senderDto: UserDto, receiverDto: UserDto): String? {
        return if (receiverDto.isResidence) receiverDto.id else senderDto.id
    }

    suspend fun findOneChatOrCreate(sender: UserDto, receiver: UserDto): Result<ChatDto> {
        val adminId = getAdminId(sender, receiver)
        val userId = getUserId(sender, receiver)

        val chatId = generateChatId("$adminId$userId")

        val chat = realm.query<Chat>("_id == $0", chatId)
            .find()
            .firstOrNull()

        val chatDto = ChatDto(
            id = chat?._id?.toHexString() ?: chatId.toHexString(),
            user1Id = adminId!!,
            user2Id = userId!!,
            lastMessage = chat?.lastMessage ?: "",
            isRead = sender.isAdmin,
            updatedAt = Clock.System.now()
        )
        if (chat == null) {
            return realm.write {
                val newChat = copyToRealm(chatDto.toEntity(), updatePolicy = UpdatePolicy.ALL)
                Result.success(newChat.toDTO())
            }
        } else if (sender.isAdmin) {
            return realm.write {
                val updatedChat = copyToRealm(chatDto.toEntity(), updatePolicy = UpdatePolicy.ALL)
                Log.d("micool", "chat: ${updatedChat.isRead}")
                Result.success(updatedChat.toDTO())
            }
        }

        return Result.success(chatDto)
    }

    fun fetchDirectMessages(sender: UserDto, receiver: UserDto): Flow<List<MessageDto>> {
        val adminId = getAdminId(sender, receiver)
        val userId = getUserId(sender, receiver)

        val chatId = generateChatId("$adminId$userId")

        return realm.query<Message>("chatId == $0", chatId)
            .sort("createdAt", Sort.DESCENDING)
            .find()
            .asFlow()
            .map { resultsChange ->
                resultsChange.list.map { it.toDto() }
            }
    }

    fun fetchUsers(userId: ObjectId): Flow<List<UserChatDto>> {
        return realm.query<User>("createdById == $0 AND isArchive == false", userId)
            .sort("firstName", Sort.DESCENDING)
            .find()
            .asFlow()
            .map { resultsChange ->
                resultsChange.list.map { user ->
                    val chatId = generateChatId("${userId.toHexString()}${user._id.toHexString()}")
                    val chat = realm.query<Chat>(
                        "_id == $0",
                        chatId
                    ).find().firstOrNull()

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
    suspend fun message(sender: UserDto, receiver: UserDto, content: String): Result<MessageDto> {
        val adminId = getAdminId(sender, receiver)
        val userId = getUserId(sender, receiver)

        return try {
            val messageDto = MessageDto(
                id = null,
                chatId = generateChatId("$adminId$userId").toHexString(),
                senderId = sender.id!!,
                receiverId = receiver.id!!,
                content = content,
                createdAt = Clock.System.now()
            )
            val chatDto = findOneChat(messageDto.chatId.toObjectId())

            realm.write {
                val message = copyToRealm(messageDto.toEntity(), updatePolicy = UpdatePolicy.ALL)
                val chat = chatDto.toEntity().apply {
                    lastMessage = content
                    updatedAt = RealmInstant.now()
                }
                chat.isRead = sender.isAdmin
                copyToRealm(chat, updatePolicy = UpdatePolicy.ALL)
                Result.success(message.toDto())
            }
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}