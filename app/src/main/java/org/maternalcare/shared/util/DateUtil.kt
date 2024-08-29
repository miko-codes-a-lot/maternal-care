package org.maternalcare.shared.util

import io.realm.kotlin.types.RealmInstant
import java.time.Instant

object DateUtil {
    fun from(date: RealmInstant): String {
        val instant = Instant.ofEpochSecond(date.epochSeconds, date.nanosecondsOfSecond.toLong())
        return instant.toString()
    }

    fun parse(date: String): RealmInstant {
        val instant = Instant.parse(date)
        return RealmInstant.from(instant.epochSecond, instant.nano)
    }
}