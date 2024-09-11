package org.maternalcare.modules.main.user.model.entity

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class User : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var firstName: String = ""
    var middleName: String? = ""
    var lastName: String = ""
    var email: String? = ""
    var mobileNumber: String? = ""
    var dateOfBirth: RealmInstant = RealmInstant.now()
    var password: String = ""
    var createdBy: User? = null
    var createdAt: RealmInstant = RealmInstant.now()
    var lastUpdatedBy: User? = null
    var lastUpdatedAt: RealmInstant = RealmInstant.now()
    var deletedBy: User? = null
    var deletedAt: RealmInstant? = null
    var isActive: Boolean = true
    var isSuperAdmin: Boolean = false
    var isAdmin: Boolean = false
    var isResidence: Boolean = false
}