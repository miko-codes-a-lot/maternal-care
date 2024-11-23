package org.maternalcare.modules.main.message.model.entity

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class MessageDeprecate: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()

    var senderId: ObjectId? = null

    var receiverId: ObjectId? = null

    var content: String? = ""

    var date: RealmInstant? = RealmInstant.now()

    var isRead: Boolean = false
}