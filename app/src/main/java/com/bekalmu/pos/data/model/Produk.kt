package com.bekalmu.pos.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "produk")
data class Produk(
    @PrimaryKey(autoGenerate = true)
    val produkId: Int = 0,
    val nama: String,
    val kategori: String,
    val hargaJual: Long,
    val hargaBeli: Long = 0,
    val stok: Int = 0,
    val deskripsi: String = "",
    val imagePath: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable
