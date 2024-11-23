package org.maternalcare.shared.ext

import io.realm.kotlin.types.RealmInstant
import kotlinx.datetime.Instant

fun Instant.toRealmInstant(): RealmInstant {
    return RealmInstant.from(epochSeconds, nanosecondsOfSecond)
}