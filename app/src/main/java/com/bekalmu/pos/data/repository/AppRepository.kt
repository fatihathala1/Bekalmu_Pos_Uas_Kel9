package com.bekalmu.pos.data.repository

import com.bekalmu.pos.data.dao.KasDao
import com.bekalmu.pos.data.dao.ProdukDao
import com.bekalmu.pos.data.dao.TransaksiDao
import com.bekalmu.pos.data.dao.UserDao
import com.bekalmu.pos.data.model.*
import com.bekalmu.pos.utils.HashUtils

class AppRepository(
    private val userDao: UserDao,
    private val produkDao: ProdukDao,
    private val transaksiDao: TransaksiDao,
    private val kasDao: KasDao
) {
    // ---- USER ----
    suspend fun login(username: String, password: String): User? =
        userDao.login(username, HashUtils.hash(password))
    fun getAllUsers() = userDao.getAllUsers()
    suspend fun insertUser(user: User) = userDao.insert(user)
    suspend fun updateUser(user: User) = userDao.update(user)
    suspend fun deleteUser(user: User) = userDao.delete(user)
    suspend fun getUserById(id: Int) = userDao.getUserById(id)

    // ---- PRODUK ----
    fun getAllProduk() = produkDao.getAllProduk()
    fun getProdukByKategori(kategori: String) = produkDao.getProdukByKategori(kategori)
    fun searchProduk(query: String) = produkDao.searchProduk(query)
    fun getTotalStok() = produkDao.getTotalStok()
    fun getTotalMenu() = produkDao.getTotalMenu()
    suspend fun insertProduk(produk: Produk) = produkDao.insert(produk)
    suspend fun updateProduk(produk: Produk) = produkDao.update(produk)
    suspend fun deleteProduk(produk: Produk) = produkDao.delete(produk)
    suspend fun getProdukById(id: Int) = produkDao.getProdukById(id)
    suspend fun getAllKategori() = produkDao.getAllKategori()
    suspend fun kurangiStok(produkId: Int, jumlah: Int) = produkDao.kurangiStok(produkId, jumlah)
    suspend fun tambahStok(produkId: Int, jumlah: Int) = produkDao.tambahStok(produkId, jumlah)

    // ---- TRANSAKSI ----
    fun getAllTransaksiWithDetails() = transaksiDao.getAllTransaksiWithDetails()
    fun getTransaksiByDate(startOfDay: Long, endOfDay: Long) = transaksiDao.getTransaksiByDate(startOfDay, endOfDay)
    fun getTransaksiSince(startDate: Long) = transaksiDao.getTransaksiSince(startDate)
    fun getTransaksiCountToday(startOfDay: Long, endOfDay: Long) = transaksiDao.getTransaksiCountToday(startOfDay, endOfDay)
    fun getPendapatanToday(startOfDay: Long, endOfDay: Long) = transaksiDao.getPendapatanToday(startOfDay, endOfDay)
    suspend fun getPendapatanRange(startDate: Long, endDate: Long) = transaksiDao.getPendapatanRange(startDate, endDate)
    suspend fun getTotalHppRange(startDate: Long, endDate: Long) = transaksiDao.getTotalHppRange(startDate, endDate)
    suspend fun getTransaksiById(id: Int) = transaksiDao.getTransaksiById(id)
    // Rekap kasir - sync version
    suspend fun getTransaksiSinceSync(startDate: Long, endDate: Long) =
        transaksiDao.getTransaksiSinceSync(startDate, endDate)

    suspend fun insertTransaksiWithDetails(transaksi: Transaksi, details: List<DetailTransaksi>): Long {
        val id = transaksiDao.insertTransaksi(transaksi)
        val detailsWithId = details.map { it.copy(transaksiId = id.toInt()) }
        transaksiDao.insertAllDetails(detailsWithId)
        detailsWithId.forEach { detail ->
            produkDao.kurangiStok(detail.produkId, detail.jumlah)
        }
        return id
    }

    suspend fun cancelTransaksi(transaksi: Transaksi) {
        transaksiDao.updateTransaksi(transaksi.copy(status = "batal"))
    }

    // ---- KAS ----
    fun getAllKas() = kasDao.getAllKas()
    fun getKasByJenis(jenis: String) = kasDao.getKasByJenis(jenis)
    fun getTotalMasuk() = kasDao.getTotalMasuk()
    fun getTotalKeluar() = kasDao.getTotalKeluar()
    suspend fun getTotalMasukRange(startDate: Long, endDate: Long) = kasDao.getTotalMasukRange(startDate, endDate)
    suspend fun getTotalKeluarRange(startDate: Long, endDate: Long) = kasDao.getTotalKeluarRange(startDate, endDate)
    fun getKasByDateRange(startDate: Long, endDate: Long) = kasDao.getKasByDateRange(startDate, endDate)
    suspend fun insertKas(kas: Kas) = kasDao.insert(kas)
    suspend fun updateKas(kas: Kas) = kasDao.update(kas)
    suspend fun deleteKas(kas: Kas) = kasDao.delete(kas)
}