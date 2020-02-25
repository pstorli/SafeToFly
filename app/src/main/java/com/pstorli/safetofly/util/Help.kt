package com.pstorli.safetofly.util

import java.util.*


class Help {

    /**
     * Instance of SafeToFlyViewModel
     */
    companion object {
        lateinit var inst: Help

        val instance: Help
            get() {
                return inst
            }
    }

    init {
        inst = this
    }

    /**
     * Convert millis to localdatetime
     *
    fun toDate (millis: Long): LocalDateTime {
    val instant: Instant = Instant.ofEpochMilli(millis)
    return instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
    }*/

    /**
     * Convert millis to localdatetime
     */
    fun toDate(millis: Long): Date {
        return Date(millis)
    }
}