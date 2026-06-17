package com.bekalmu.pos.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("bekalmu_session", Context.MODE_PRIVATE)

    companion object {
        const val KEY_USER_ID = "user_id"
        const val KEY_USER_NAME = "user_name"
        const val KEY_USER_ROLE = "user_role"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val KEY_CABANG = "kode_cabang"
    }

    fun saveSession(userId: Int, nama: String, role: String, kodeCabang: String) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, nama)
            putString(KEY_USER_ROLE, role)
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_CABANG, kodeCabang)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)
    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "") ?: ""
    fun getUserRole(): String = prefs.getString(KEY_USER_ROLE, "") ?: ""
    fun getKodeCabang(): String = prefs.getString(KEY_CABANG, "BKL-JKT01") ?: "BKL-JKT01"

    fun isOwner(): Boolean = getUserRole() == "owner"

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
