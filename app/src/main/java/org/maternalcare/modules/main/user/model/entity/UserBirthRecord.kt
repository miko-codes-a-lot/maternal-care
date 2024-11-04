package org.maternalcare.modules.main.user.model.entity

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class UserBirthRecord  : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var pregnancyUserId: String = ""
    var childOrder: Int = 0
    var pregnancyOrder: Int = 0
    var fillDate: RealmInstant = RealmInstant.now()
}