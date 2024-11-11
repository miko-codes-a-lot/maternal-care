package org.maternalcare.modules.main.message.service

import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.maternalcare.modules.main.message.mapper.toDTO
import org.maternalcare.modules.main.message.mapper.toEntity
import org.maternalcare.modules.main.message.model.dto.MessageDto
import org.maternalcare.modules.main.message.model.entity.Message
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class MessageService @Inject constructor(private val realm: Realm) {
    fun fetch(senderId: ObjectId, receiverId: ObjectId): List<MessageDto> {
        return realm.query<Message>("senderId == $0 AND receiverId == $1", senderId, receiverId)
            .find()
            .map { message -> message.toDTO() }
    }

    fun fetchAsFlow(senderId: ObjectId, receiverId: ObjectId): Flow<ResultsChange<Message>> {
        val query = StringBuilder()
            .append("senderId == $0 AND receiverId == $1")
            .append(" OR ")
            .append("senderId == $2 AND receiverId == $3")
            .toString()
        return realm.query<Message>(query, senderId, receiverId, receiverId, senderId)
            .sort("_id", Sort.DESCENDING)
            .find()
            .asFlow()
    }

    suspend fun send(messageDto: MessageDto): Result<MessageDto> {
        return try {
            realm.write {
                messageDto.isRead = false
                val message = copyToRealm(messageDto.toEntity(), updatePolicy = UpdatePolicy.ALL)
                Result.success(message.toDTO())
            }
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    fun fetchLatestMessage(senderId: ObjectId, receiverId: ObjectId): Flow<MessageDto?> {
        val query = realm.query<Message>(
            "senderId == $0 AND receiverId == $1 OR senderId == $1 AND receiverId == $0",
            senderId, receiverId
        ).sort("date", Sort.DESCENDING)

        return query.asFlow().map { results ->
            results.list.firstOrNull()?.toDTO()
        }
    }

    suspend fun updateMessageReadStatus(messageDto: MessageDto) {
        realm.write {
            val messageId = ObjectId(messageDto.id ?: throw IllegalArgumentException("Invalid message ID"))
            val message = query<Message>("_id == $0", messageId).first().find()
            if (message != null) {
                message.isRead = true
                copyToRealm(message, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }
}