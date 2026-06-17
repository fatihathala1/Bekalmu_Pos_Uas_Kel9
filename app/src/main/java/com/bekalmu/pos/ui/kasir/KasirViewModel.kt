package com.bekalmu.pos.ui.kasir

import androidx.lifecycle.*
import com.bekalmu.pos.data.model.*
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.utils.DateUtils
import kotlinx.coroutines.launch

class KasirViewModel(private val repository: AppRepository) : ViewModel() {

    val allProduk = repository.getAllProduk()

    private val _selectedKategori = MutableLiveData<String>("Semua")
    val selectedKategori: LiveData<String> = _selectedKategori

    private val _searchQuery = MutableLiveData<String>("")

    private val _cartItems = MutableLiveData<MutableList<CartItem>>(mutableListOf())
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems

    val cartTotal: LiveData<Long> = _cartItems.map { items ->
        items.sumOf { it.subtotal }
    }

    val cartCount: LiveData<Int> = _cartItems.map { items ->
        items.sumOf { it.jumlah }
    }

    private val _transaksiResult = MutableLiveData<TransaksiResult?>()
    val transaksiResult: LiveData<TransaksiResult?> = _transaksiResult

    val filteredProduk: LiveData<List<Produk>> = MediatorLiveData<List<Produk>>().apply {
        var kategori = "Semua"
        var query = ""
        var produkList: List<Produk> = emptyList()

        fun update() {
            var filtered = if (kategori == "Semua") produkList
            else produkList.filter { it.kategori == kategori }
            if (query.isNotEmpty()) {
                filtered = filtered.filter { it.nama.contains(query, ignoreCase = true) }
            }
            value = filtered
        }

        addSource(allProduk) { list -> produkList = list; update() }
        addSource(_selectedKategori) { kat -> kategori = kat; update() }
        addSource(_searchQuery) { q -> query = q; update() }
    }

    fun setKategori(kategori: String) {
        _selectedKategori.value = kategori
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addToCart(produk: Produk) {
        val currentList = _cartItems.value ?: mutableListOf()
        val existingIndex = currentList.indexOfFirst { it.produk.produkId == produk.produkId }
        if (existingIndex != -1) {
            val existingItem = currentList[existingIndex]
            if (existingItem.jumlah < produk.stok) {
                // Buat objek CartItem baru (bukan mutasi langsung) supaya DiffUtil
                // bisa mendeteksi perubahan jumlah dan me-refresh tampilan dengan benar
                currentList[existingIndex] = existingItem.copy(jumlah = existingItem.jumlah + 1)
            }
        } else {
            if (produk.stok > 0) {
                currentList.add(CartItem(produk))
            }
        }
        _cartItems.value = currentList
    }

    fun removeFromCart(produk: Produk) {
        val currentList = _cartItems.value ?: mutableListOf()
        val existingIndex = currentList.indexOfFirst { it.produk.produkId == produk.produkId }
        if (existingIndex != -1) {
            val existingItem = currentList[existingIndex]
            if (existingItem.jumlah > 1) {
                // Buat objek CartItem baru (bukan mutasi langsung) supaya DiffUtil
                // bisa mendeteksi perubahan jumlah dan me-refresh tampilan dengan benar
                currentList[existingIndex] = existingItem.copy(jumlah = existingItem.jumlah - 1)
            } else {
                currentList.removeAt(existingIndex)
            }
        }
        _cartItems.value = currentList
    }

    fun clearTransaksiResult() {
        _transaksiResult.value = null
    }

    fun clearCart() {
        _cartItems.value = mutableListOf()
    }

    fun processTransaksi(
        userId: Int,
        namaKasir: String,
        metodePembayaran: String,
        uangDiterima: Long
    ) {
        val items = _cartItems.value ?: return
        if (items.isEmpty()) return

        val total = cartTotal.value ?: 0L
        if (metodePembayaran == "tunai" && uangDiterima < total) {
            _transaksiResult.value = TransaksiResult.Error("Uang yang diterima kurang")
            return
        }

        viewModelScope.launch {
            try {
                val nomorOrder = DateUtils.generateOrderNumber()
                val transaksi = Transaksi(
                    nomorOrder = nomorOrder,
                    userId = userId,
                    namaKasir = namaKasir,
                    totalHarga = total,
                    metodePembayaran = metodePembayaran,
                    uangDiterima = uangDiterima,
                    kembalian = if (metodePembayaran == "tunai") uangDiterima - total else 0
                )
                val details = items.map { cartItem ->
                    DetailTransaksi(
                        transaksiId = 0,
                        produkId = cartItem.produk.produkId,
                        namaProduk = cartItem.produk.nama,
                        hargaSatuan = cartItem.produk.hargaJual,
                        jumlah = cartItem.jumlah,
                        subtotal = cartItem.subtotal
                    )
                }
                val id = repository.insertTransaksiWithDetails(transaksi, details)
                clearCart()
                _transaksiResult.value = TransaksiResult.Success(
                    transaksi.copy(transaksiId = id.toInt()),
                    details
                )
            } catch (e: Exception) {
                _transaksiResult.value = TransaksiResult.Error("Gagal menyimpan transaksi: ${e.message}")
            }
        }
    }

    sealed class TransaksiResult {
        data class Success(val transaksi: Transaksi, val details: List<DetailTransaksi>) : TransaksiResult()
        data class Error(val message: String) : TransaksiResult()
    }
}