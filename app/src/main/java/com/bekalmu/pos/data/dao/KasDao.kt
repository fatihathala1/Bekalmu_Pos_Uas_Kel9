package com.bekalmu.pos.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bekalmu.pos.data.model.Kas

@Dao
interface KasDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kas: Kas): Long

    @Update
    suspend fun update(kas: Kas)

    @Delete
    suspend fun delete(kas: Kas)

    @Query("SELECT * FROM kas ORDER BY tanggal DESC")
    fun getAllKas(): LiveData<List<Kas>>

    @Query("SELECT * FROM kas WHERE jenis = :jenis ORDER BY tanggal DESC")
    fun getKasByJenis(jenis: String): LiveData<List<Kas>>

    @Query("SELECT SUM(jumlah) FROM kas WHERE jenis = 'masuk'")
    fun getTotalMasuk(): LiveData<Long?>

    @Query("SELECT SUM(jumlah) FROM kas WHERE jenis = 'keluar'")
    fun getTotalKeluar(): LiveData<Long?>

    @Query("SELECT SUM(jumlah) FROM kas WHERE jenis = 'masuk' AND tanggal >= :startDate AND tanggal <= :endDate")
    suspend fun getTotalMasukRange(startDate: Long, endDate: Long): Long?

    @Query("SELECT SUM(jumlah) FROM kas WHERE jenis = 'keluar' AND tanggal >= :startDate AND tanggal <= :endDate")
    suspend fun getTotalKeluarRange(startDate: Long, endDate: Long): Long?

    @Query("SELECT * FROM kas WHERE tanggal >= :startDate AND tanggal <= :endDate ORDER BY tanggal DESC")
    fun getKasByDateRange(startDate: Long, endDate: Long): LiveData<List<Kas>>
}
