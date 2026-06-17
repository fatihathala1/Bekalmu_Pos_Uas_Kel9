package com.bekalmu.pos.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detail_transaksi")
data class DetailTransaksi(
    @PrimaryKey(autoGenerate = true)
    val detailId: Int = 0,
    val transaksiId: Int,
    val produkId: Int,
    val namaProduk: String,
    val hargaSatuan: Long,
    val jumlah: Int,
    val subtotal: Long
)
