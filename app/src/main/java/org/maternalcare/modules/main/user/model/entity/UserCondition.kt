package org.maternalcare.modules.main.user.model.entity

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class UserCondition : RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()
    var userId: String = ""
    var tuberculosisPersonal: Boolean = false
    var tuberculosisFamily: Boolean = false
    var heartDiseasesPersonal: Boolean = false
    var heartDiseasesFamily: Boolean = false
    var diabetesPersonal: Boolean = false
    var diabetesFamily: Boolean = false
    var hypertensionPersonal: Boolean = false
    var hypertensionFamily: Boolean = false
    var branchialAsthmaPersonal: Boolean = false
    var branchialAsthmaFamily: Boolean = false
    var urinaryTractInfectionPersonal: Boolean = false
    var urinaryTractInfectionFamily: Boolean = false
    var parasitismPersonal: Boolean = false
    var parasitismFamily: Boolean = false
    var goitersPersonal: Boolean = false
    var goitersFamily: Boolean = false
    var anemiaPersonal: Boolean = false
    var anemiaFamily: Boolean = false
    var isUnderWeight: Boolean = false
    var isOverWeight: Boolean = false
    var genitalTractInfection: String? = ""
    var otherInfectionsDiseases: String? = ""
    var createdById: ObjectId? = null
    var createdAt: RealmInstant = RealmInstant.now()
    var lastUpdatedById: ObjectId? = null
    var lastUpdatedAt: RealmInstant = RealmInstant.now()
    var deletedById: ObjectId? = null
    var deletedAt: RealmInstant? = null
}