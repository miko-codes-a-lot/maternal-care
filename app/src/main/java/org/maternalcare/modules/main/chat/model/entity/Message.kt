package org.maternalcare.modules.main.chat.model.entity

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class Message: RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()
    var chatId: ObjectId? = null
    var senderId: ObjectId? = null
    var receiverId: ObjectId? = null
    var content: String = ""
    var createdAt: RealmInstant = RealmInstant.now()
}