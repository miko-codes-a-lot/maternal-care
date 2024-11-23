package org.maternalcare.shared.ext

import io.realm.kotlin.types.RealmInstant
import java.time.Instant
import kotlinx.datetime.Instant as InstantK

fun RealmInstant.toInstantString(): String {
    val instant = Instant.ofEpochSecond(epochSeconds, nanosecondsOfSecond.toLong())
    return instant.toString()
}

fun RealmInstant.toInstantK(): InstantK {
    return InstantK.fromEpochSeconds(epochSeconds, nanosecondsOfSecond.toLong())
}

fun RealmInstant?.toInstantStringNullable(): String? {
    if (this == null) return null

    val instant = Instant.ofEpochSecond(epochSeconds, nanosecondsOfSecond.toLong())
    return instant.toString()
}
