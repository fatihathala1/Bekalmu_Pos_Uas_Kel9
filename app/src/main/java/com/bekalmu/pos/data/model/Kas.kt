package com.bekalmu.pos.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kas")
data class Kas(
    @PrimaryKey(autoGenerate = true)
    val kasId: Int = 0,
    val userId: Int,
    val jenis: String, // "masuk" or "keluar"
    val kategori: String,
    val jumlah: Long,
    val catatan: String = "",
    val tanggal: Long = System.currentTimeMillis()
)
