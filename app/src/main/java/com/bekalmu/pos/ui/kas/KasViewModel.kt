package com.bekalmu.pos.ui.kas

import androidx.lifecycle.*
import com.bekalmu.pos.data.model.Kas
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.utils.DateUtils
import kotlinx.coroutines.launch
import java.util.Calendar

class KasViewModel(private val repository: AppRepository) : ViewModel() {

    val totalMasuk = repository.getTotalMasuk()
    val totalKeluar = repository.getTotalKeluar()

    val saldo: LiveData<Long> = MediatorLiveData<Long>().apply {
        var masuk = 0L
        var keluar = 0L
        addSource(totalMasuk) { m -> masuk = m ?: 0L; value = masuk - keluar }
        addSource(totalKeluar) { k -> keluar = k ?: 0L; value = masuk - keluar }
    }

    val allKas = repository.getAllKas()

    private val _operationResult = MutableLiveData<String?>()
    val operationResult: LiveData<String?> = _operationResult

    fun getKasByJenis(jenis: String) = repository.getKasByJenis(jenis)

    fun getKasBulanIni(): LiveData<List<Kas>> {
        val start = getStartOfMonth()
        val end = System.currentTimeMillis()
        return repository.getKasByDateRange(start, end)
    }

    fun insertKas(kas: Kas) {
        viewModelScope.launch {
            try {
                repository.insertKas(kas)
                _operationResult.value = "Kas berhasil disimpan"
            } catch (e: Exception) {
                _operationResult.value = "Gagal: ${e.message}"
            }
        }
    }

    fun updateKas(kas: Kas) {
        viewModelScope.launch {
            try {
                repository.updateKas(kas)
                _operationResult.value = "Kas berhasil diperbarui"
            } catch (e: Exception) {
                _operationResult.value = "Gagal: ${e.message}"
            }
        }
    }

    fun deleteKas(kas: Kas) {
        viewModelScope.launch {
            try {
                repository.deleteKas(kas)
                _operationResult.value = "Kas berhasil dihapus"
            } catch (e: Exception) {
                _operationResult.value = "Gagal: ${e.message}"
            }
        }
    }

    fun clearResult() { _operationResult.value = null }

    private fun getStartOfMonth(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
