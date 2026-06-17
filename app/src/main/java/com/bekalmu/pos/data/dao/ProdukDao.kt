package com.bekalmu.pos.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bekalmu.pos.data.model.Produk

@Dao
interface ProdukDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(produk: Produk): Long

    @Update
    suspend fun update(produk: Produk)

    @Delete
    suspend fun delete(produk: Produk)

    @Query("SELECT * FROM produk WHERE isActive = 1 ORDER BY nama ASC")
    fun getAllProduk(): LiveData<List<Produk>>

    @Query("SELECT * FROM produk WHERE isActive = 1 AND kategori = :kategori ORDER BY nama ASC")
    fun getProdukByKategori(kategori: String): LiveData<List<Produk>>

    @Query("SELECT * FROM produk WHERE isActive = 1 AND nama LIKE '%' || :query || '%' ORDER BY nama ASC")
    fun searchProduk(query: String): LiveData<List<Produk>>

    @Query("SELECT * FROM produk WHERE produkId = :id LIMIT 1")
    suspend fun getProdukById(id: Int): Produk?

    @Query("SELECT SUM(stok) FROM produk WHERE isActive = 1")
    fun getTotalStok(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM produk WHERE isActive = 1")
    fun getTotalMenu(): LiveData<Int>

    @Query("UPDATE produk SET stok = stok - :jumlah WHERE produkId = :produkId")
    suspend fun kurangiStok(produkId: Int, jumlah: Int)

    @Query("UPDATE produk SET stok = stok + :jumlah WHERE produkId = :produkId")
    suspend fun tambahStok(produkId: Int, jumlah: Int)

    @Query("SELECT DISTINCT kategori FROM produk WHERE isActive = 1")
    suspend fun getAllKategori(): List<String>
}
