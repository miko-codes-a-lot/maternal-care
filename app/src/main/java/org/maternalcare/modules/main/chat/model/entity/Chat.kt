package org.maternalcare.modules.main.chat.model.entity

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class Chat: RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()
    var user1Id: ObjectId? = null
    var user2Id: ObjectId? = null
    var lastMessage: String = ""
    var isRead: Boolean = false
    var updatedAt: RealmInstant = RealmInstant.now()
}