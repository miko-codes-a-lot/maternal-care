package org.maternalcare.modules.main.user.model.entity

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class UserCheckup : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var userId: String = ""
    var bloodPressure: Double = 0.0
    var height: Double = 0.0
    var weight: Double = 0.0
    var lastMenstrualPeriod: RealmInstant = RealmInstant.now()
    var dateOfCheckUp: RealmInstant = RealmInstant.now()
    var scheduleOfNextCheckUp: RealmInstant = RealmInstant.now()
    var checkup: Int = 1
}