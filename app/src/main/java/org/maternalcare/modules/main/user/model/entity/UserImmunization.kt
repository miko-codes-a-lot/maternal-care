package org.maternalcare.modules.main.user.model.entity

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class UserImmunization : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var userId: String = ""
    var firstDoseGiven: RealmInstant = RealmInstant.now()
    var firstDoseReturn: RealmInstant = RealmInstant.now()
    var secondDoseGiven: RealmInstant = RealmInstant.now()
    var secondDoseReturn: RealmInstant = RealmInstant.now()
    var thirdDoseGiven: RealmInstant = RealmInstant.now()
    var thirdDoseReturn: RealmInstant = RealmInstant.now()
    var fourthDoseGiven: RealmInstant = RealmInstant.now()
    var fourthDoseReturn: RealmInstant = RealmInstant.now()
    var fifthDoseGiven: RealmInstant = RealmInstant.now()
    var fifthDoseReturn: RealmInstant = RealmInstant.now()
    var createdById: ObjectId? = null
    var createdAt: RealmInstant = RealmInstant.now()
    var lastUpdatedById: ObjectId? = null
    var lastUpdatedAt: RealmInstant = RealmInstant.now()
    var deletedById: ObjectId? = null
    var deletedAt: RealmInstant? = null
}