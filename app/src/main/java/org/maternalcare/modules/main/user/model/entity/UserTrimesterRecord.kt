package org.maternalcare.modules.main.user.model.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class UserTrimesterRecord : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var trimesterUserId: String = ""
    var pregnancyUserId: String = ""
    var trimesterOrder: Int = 0
}