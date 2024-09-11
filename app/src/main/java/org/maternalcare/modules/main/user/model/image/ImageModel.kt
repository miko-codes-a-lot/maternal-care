package org.maternalcare.modules.main.user.model.image

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class ImageModel : RealmObject {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var imageData: ByteArray = byteArrayOf()
}