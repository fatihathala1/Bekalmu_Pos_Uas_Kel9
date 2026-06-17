package com.bekalmu.pos.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaksi")
data class Transaksi(
    @PrimaryKey(autoGenerate = true)
    val transaksiId: Int = 0,
    val nomorOrder: String,
    val userId: Int,
    val namaKasir: String,
    val totalHarga: Long,
    val metodePembayaran: String, // "tunai" or "qris"
    val uangDiterima: Long = 0,
    val kembalian: Long = 0,
    val status: String = "sukses", // "sukses" or "batal"
    val tanggal: Long = System.currentTimeMillis()
)
