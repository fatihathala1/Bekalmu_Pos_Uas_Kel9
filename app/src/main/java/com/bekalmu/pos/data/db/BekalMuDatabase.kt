package com.bekalmu.pos.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bekalmu.pos.data.dao.KasDao
import com.bekalmu.pos.data.dao.ProdukDao
import com.bekalmu.pos.data.dao.TransaksiDao
import com.bekalmu.pos.data.dao.UserDao
import com.bekalmu.pos.data.model.*
import com.bekalmu.pos.utils.HashUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Produk::class, Transaksi::class, DetailTransaksi::class, Kas::class],
    version = 3,
    exportSchema = false
)
abstract class BekalMuDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun produkDao(): ProdukDao
    abstract fun transaksiDao(): TransaksiDao
    abstract fun kasDao(): KasDao

    companion object {
        @Volatile
        private var INSTANCE: BekalMuDatabase? = null

        fun getDatabase(context: Context): BekalMuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BekalMuDatabase::class.java,
                    "bekalmu_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database)
                }
            }
        }

        suspend fun populateDatabase(db: BekalMuDatabase) {
            // Insert default owner
            db.userDao().insert(
                User(
                    nama = "Admin Owner",
                    username = "owner",
                    password = HashUtils.hash("owner123"),
                    role = "owner"
                )
            )
            // Insert default kasir
            db.userDao().insert(
                User(
                    nama = "Siti Aminah",
                    username = "kasir",
                    password = HashUtils.hash("kasir123"),
                    role = "kasir"
                )
            )

            // Insert sample products
            val produkList = listOf(
                Produk(nama = "Es Coklat", kategori = "Minuman", hargaJual = 10000, hargaBeli = 5000, stok = 45, imagePath = "drawable://menu_es_coklat"),
                Produk(nama = "Es Teh Manis", kategori = "Minuman", hargaJual = 10000, hargaBeli = 3000, stok = 60, imagePath = "drawable://menu_es_teh"),
                Produk(nama = "Air Mineral", kategori = "Minuman", hargaJual = 5000, hargaBeli = 2000, stok = 100, imagePath = "drawable://menu_air_mineral"),
                Produk(nama = "Lumpia (3pcs)", kategori = "Snack & Cemilan", hargaJual = 10000, hargaBeli = 5000, stok = 120, imagePath = "drawable://menu_lumpia"),
                Produk(nama = "Pisang Goreng (3pcs)", kategori = "Snack & Cemilan", hargaJual = 10000, hargaBeli = 4000, stok = 80, imagePath = "drawable://menu_pisang_goreng"),
                Produk(nama = "Nasi Ayam Goreng", kategori = "Nasi & Lauk", hargaJual = 10000, hargaBeli = 6000, stok = 32, imagePath = "drawable://menu_nasi_ayam_goreng"),
                Produk(nama = "Nasi Kuning", kategori = "Nasi & Lauk", hargaJual = 10000, hargaBeli = 5000, stok = 50, imagePath = "drawable://menu_nasi_kuning"),
                Produk(nama = "Nasi Ayam Bakar", kategori = "Nasi & Lauk", hargaJual = 10000, hargaBeli = 6500, stok = 40, imagePath = "drawable://menu_nasi_ayam_bakar"),
                Produk(nama = "Ayam Gulai", kategori = "Lauk", hargaJual = 10000, hargaBeli = 5000, stok = 25, imagePath = "drawable://menu_ayam_gulai"),
                Produk(nama = "Ayam Kentaki", kategori = "Lauk", hargaJual = 10000, hargaBeli = 5500, stok = 35, imagePath = "drawable://menu_ayam_kentaki"),
                Produk(nama = "Kentang Goreng", kategori = "Lauk", hargaJual = 10000, hargaBeli = 4000, stok = 15, imagePath = "drawable://menu_kentang_goreng"),
                Produk(nama = "Telur Balado", kategori = "Lauk", hargaJual = 10000, hargaBeli = 4500, stok = 45, imagePath = "drawable://menu_telur_balado")
            )
            produkList.forEach { db.produkDao().insert(it) }
        }
    }
}
