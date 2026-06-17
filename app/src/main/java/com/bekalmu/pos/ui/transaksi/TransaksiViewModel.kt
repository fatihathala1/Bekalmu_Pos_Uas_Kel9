package com.bekalmu.pos.ui.transaksi

import androidx.lifecycle.*
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.utils.DateUtils
import java.util.Calendar

class TransaksiViewModel(private val repository: AppRepository) : ViewModel() {

    fun getAllTransaksi() = repository.getAllTransaksiWithDetails()

    fun getTransaksiByMonthOffset(offset: Int) = repository.getTransaksiSince(
        getStartOfMonthWithOffset(offset)
    )

    fun getTransaksiByDate(dateMs: Long): androidx.lifecycle.LiveData<List<com.bekalmu.pos.data.model.TransaksiWithDetails>> {
        val start = DateUtils.getStartOfDay(java.util.Date(dateMs))
        val end = DateUtils.getEndOfDay(java.util.Date(dateMs))
        return repository.getTransaksiByDate(start, end)
    }

    private fun getStartOfMonthWithOffset(offset: Int): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -offset)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        return cal.timeInMillis
    }
}
