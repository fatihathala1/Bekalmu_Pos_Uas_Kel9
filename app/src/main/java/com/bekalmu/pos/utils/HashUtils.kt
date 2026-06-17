package com.bekalmu.pos.utils

import java.security.MessageDigest

object HashUtils {
    fun hash(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
