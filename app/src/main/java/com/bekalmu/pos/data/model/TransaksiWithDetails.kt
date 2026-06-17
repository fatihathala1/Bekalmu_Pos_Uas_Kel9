package com.bekalmu.pos.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class TransaksiWithDetails(
    @Embedded val transaksi: Transaksi,
    @Relation(
        parentColumn = "transaksiId",
        entityColumn = "transaksiId"
    )
    val details: List<DetailTransaksi>
)

data class CartItem(
    val produk: Produk,
    var jumlah: Int = 1
) {
    val subtotal: Long get() = produk.hargaJual * jumlah
}

data class DailySummary(
    val tanggal: Long,
    val totalTransaksi: Int,
    val totalPendapatan: Long
)
