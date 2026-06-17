package com.bekalmu.pos.ui.inventori

import androidx.lifecycle.*
import com.bekalmu.pos.data.model.Produk
import com.bekalmu.pos.data.repository.AppRepository
import kotlinx.coroutines.launch

class InventoriViewModel(private val repository: AppRepository) : ViewModel() {

    val allProduk = repository.getAllProduk()
    val totalStok = repository.getTotalStok()
    val totalMenu = repository.getTotalMenu()

    private val _searchQuery = MutableLiveData<String>("")
    private val _selectedKategori = MutableLiveData<String>("Semua")

    private val _operationResult = MutableLiveData<String?>()
    val operationResult: LiveData<String?> = _operationResult

    val filteredProduk: LiveData<List<Produk>> = MediatorLiveData<List<Produk>>().apply {
        var all: List<Produk> = emptyList()
        var query = ""
        var kategori = "Semua"

        fun update() {
            value = all.filter { produk ->
                val matchQuery = query.isEmpty() || produk.nama.contains(query, ignoreCase = true)
                val matchKat = kategori == "Semua" || produk.kategori == kategori
                matchQuery && matchKat
            }
        }

        addSource(allProduk) { list -> all = list; update() }
        addSource(_searchQuery) { q -> query = q; update() }
        addSource(_selectedKategori) { k -> kategori = k; update() }
    }

    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setKategori(kategori: String) { _selectedKategori.value = kategori }

    fun insertProduk(produk: Produk) {
        viewModelScope.launch {
            try {
                repository.insertProduk(produk)
                _operationResult.value = "Produk berhasil ditambahkan"
            } catch (e: Exception) {
                _operationResult.value = "Gagal: ${e.message}"
            }
        }
    }

    fun updateProduk(produk: Produk) {
        viewModelScope.launch {
            try {
                repository.updateProduk(produk)
                _operationResult.value = "Produk berhasil diperbarui"
            } catch (e: Exception) {
                _operationResult.value = "Gagal: ${e.message}"
            }
        }
    }

    fun deleteProduk(produk: Produk) {
        viewModelScope.launch {
            try {
                repository.deleteProduk(produk)
                _operationResult.value = "Produk berhasil dihapus"
            } catch (e: Exception) {
                _operationResult.value = "Gagal: ${e.message}"
            }
        }
    }

    fun restokProduk(produkId: Int, jumlah: Int) {
        viewModelScope.launch {
            try {
                repository.tambahStok(produkId, jumlah)
                _operationResult.value = "Stok berhasil diperbarui"
            } catch (e: Exception) {
                _operationResult.value = "Gagal: ${e.message}"
            }
        }
    }

    fun clearResult() { _operationResult.value = null }
}
