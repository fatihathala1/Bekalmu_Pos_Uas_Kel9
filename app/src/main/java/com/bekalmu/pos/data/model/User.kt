package com.bekalmu.pos.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val nama: String,
    val username: String,
    val password: String, // hashed
    val role: String, // "owner" or "kasir"
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
