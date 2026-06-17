package com.bekalmu.pos.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bekalmu.pos.data.model.DetailTransaksi
import com.bekalmu.pos.data.model.Transaksi
import com.bekalmu.pos.data.model.TransaksiWithDetails

@Dao
interface TransaksiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaksi(transaksi: Transaksi): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetail(detail: DetailTransaksi)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDetails(details: List<DetailTransaksi>)

    @Update
    suspend fun updateTransaksi(transaksi: Transaksi)

    @Transaction
    @Query("SELECT * FROM transaksi ORDER BY tanggal DESC")
    fun getAllTransaksiWithDetails(): LiveData<List<TransaksiWithDetails>>

    @Transaction
    @Query("SELECT * FROM transaksi WHERE tanggal >= :startOfDay AND tanggal <= :endOfDay ORDER BY tanggal DESC")
    fun getTransaksiByDate(startOfDay: Long, endOfDay: Long): LiveData<List<TransaksiWithDetails>>

    @Transaction
    @Query("SELECT * FROM transaksi WHERE tanggal >= :startDate ORDER BY tanggal DESC")
    fun getTransaksiSince(startDate: Long): LiveData<List<TransaksiWithDetails>>

    @Query("SELECT COUNT(*) FROM transaksi WHERE tanggal >= :startOfDay AND tanggal <= :endOfDay AND status = 'sukses'")
    fun getTransaksiCountToday(startOfDay: Long, endOfDay: Long): LiveData<Int>

    @Query("SELECT SUM(totalHarga) FROM transaksi WHERE tanggal >= :startOfDay AND tanggal <= :endOfDay AND status = 'sukses'")
    fun getPendapatanToday(startOfDay: Long, endOfDay: Long): LiveData<Long?>

    @Query("SELECT SUM(totalHarga) FROM transaksi WHERE tanggal >= :startDate AND tanggal <= :endDate AND status = 'sukses'")
    suspend fun getPendapatanRange(startDate: Long, endDate: Long): Long?

    @Transaction
    @Query("SELECT * FROM transaksi WHERE transaksiId = :id LIMIT 1")
    suspend fun getTransaksiById(id: Int): TransaksiWithDetails?

    @Query("SELECT * FROM detail_transaksi WHERE transaksiId = :transaksiId")
    suspend fun getDetailsByTransaksiId(transaksiId: Int): List<DetailTransaksi>

    @Query(
        """
        SELECT COALESCE(SUM(dt.jumlah * p.hargaBeli), 0)
        FROM detail_transaksi dt
        INNER JOIN transaksi t ON dt.transaksiId = t.transaksiId
        INNER JOIN produk p ON dt.produkId = p.produkId
        WHERE t.tanggal >= :startDate AND t.tanggal <= :endDate AND t.status = 'sukses'
        """
    )
    suspend fun getTotalHppRange(startDate: Long, endDate: Long): Long

    @Transaction
    @Query("SELECT * FROM transaksi WHERE tanggal >= :startDate AND tanggal <= :endDate ORDER BY tanggal DESC")
    suspend fun getTransaksiSinceSync(startDate: Long, endDate: Long): List<TransaksiWithDetails>
}