package org.maternalcare.shared.ext

import io.realm.kotlin.types.RealmInstant
import java.time.Instant

fun RealmInstant.toInstantString(): String {
    val instant = Instant.ofEpochSecond(epochSeconds, nanosecondsOfSecond.toLong())
    return instant.toString()
}

fun RealmInstant?.toInstantStringNullable(): String? {
    if (this == null) return null

    val instant = Instant.ofEpochSecond(epochSeconds, nanosecondsOfSecond.toLong())
    return instant.toString()
}
