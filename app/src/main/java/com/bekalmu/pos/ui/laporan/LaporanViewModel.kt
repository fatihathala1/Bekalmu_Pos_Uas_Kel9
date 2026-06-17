package com.bekalmu.pos.ui.laporan

import androidx.lifecycle.*
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.utils.DateUtils
import kotlinx.coroutines.launch
import java.util.Calendar

class LaporanViewModel(private val repository: AppRepository) : ViewModel() {

    val pendapatanHariIni = repository.getPendapatanToday(
        DateUtils.getStartOfDay(), DateUtils.getEndOfDay()
    )

    private val _weeklyData = MutableLiveData<List<Pair<String, Long>>>()
    val weeklyData: LiveData<List<Pair<String, Long>>> = _weeklyData

    private val _labaBersih = MutableLiveData<Long>()
    val labaBersih: LiveData<Long> = _labaBersih

    private val _rekapKasir = MutableLiveData<List<RekapKasir>>()

    init {
        loadWeeklyData()
        loadLabaBersih()
        loadRekapKasir()
    }

    fun getRekapPerKasir(): LiveData<List<RekapKasir>> = _rekapKasir

    fun loadWeeklyData() {
        viewModelScope.launch {
            val days = mutableListOf<Pair<String, Long>>()
            val dayLabels = listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab")
            val cal = Calendar.getInstance()
            for (i in 6 downTo 0) {
                cal.timeInMillis = System.currentTimeMillis()
                cal.add(Calendar.DAY_OF_YEAR, -i)
                val start = DateUtils.getStartOfDay(cal.time)
                val end = DateUtils.getEndOfDay(cal.time)
                val pendapatan = repository.getPendapatanRange(start, end) ?: 0L
                val label = dayLabels[cal.get(Calendar.DAY_OF_WEEK) - 1]
                days.add(Pair(label, pendapatan))
            }
            _weeklyData.postValue(days)
        }
    }

    fun loadLabaBersih() {
        viewModelScope.launch {
            val startMonth = getStartOfMonth()
            val now = System.currentTimeMillis()
            val pendapatan = repository.getPendapatanRange(startMonth, now) ?: 0L
            // HPP (Harga Pokok Penjualan) = total modal dari semua item yang terjual,
            // dihitung dari hargaBeli produk dikali jumlah terjual per transaksi sukses
            val totalHpp = repository.getTotalHppRange(startMonth, now)
            val pengeluaranLain = repository.getTotalKeluarRange(startMonth, now) ?: 0L
            // Laba Bersih = Pendapatan - Modal Barang Terjual - Pengeluaran Operasional Lain
            _labaBersih.postValue(pendapatan - totalHpp - pengeluaranLain)
        }
    }

    fun loadRekapKasir() {
        viewModelScope.launch {
            val startMonth = getStartOfMonth()
            val now = System.currentTimeMillis()
            val allTx = repository.getTransaksiSinceSync(startMonth, now)
            val grouped = allTx.groupBy { it.transaksi.namaKasir }
            val rekap = grouped.map { (nama, txList) ->
                RekapKasir(
                    namaKasir = nama,
                    jumlahTransaksi = txList.filter { it.transaksi.status == "sukses" }.size,
                    totalPendapatan = txList.filter { it.transaksi.status == "sukses" }
                        .sumOf { it.transaksi.totalHarga }
                )
            }.sortedByDescending { it.totalPendapatan }
            _rekapKasir.postValue(rekap)
        }
    }

    private fun getStartOfMonth(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}