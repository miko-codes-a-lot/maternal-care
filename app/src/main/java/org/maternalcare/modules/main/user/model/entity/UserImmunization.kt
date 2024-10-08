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
    var firstDoseGiven: RealmInstant? = null
    var firstDoseReturn: RealmInstant? = null
    var secondDoseGiven: RealmInstant? = null
    var secondDoseReturn: RealmInstant? = null
    var thirdDoseGiven: RealmInstant? = null
    var thirdDoseReturn: RealmInstant? = null
    var fourthDoseGiven: RealmInstant? = null
    var fourthDoseReturn: RealmInstant? = null
    var fifthDoseGiven: RealmInstant? = null
    var fifthDoseReturn: RealmInstant? = null
    var createdById: ObjectId? = null
    var createdAt: RealmInstant = RealmInstant.now()
    var lastUpdatedById: ObjectId? = null
    var lastUpdatedAt: RealmInstant = RealmInstant.now()
    var deletedById: ObjectId? = null
    var deletedAt: RealmInstant? = null
}