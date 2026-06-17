package com.bekalmu.pos.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.ui.inventori.InventoriViewModel
import com.bekalmu.pos.ui.kas.KasViewModel
import com.bekalmu.pos.ui.kasir.KasirViewModel
import com.bekalmu.pos.ui.laporan.LaporanViewModel
import com.bekalmu.pos.ui.login.LoginViewModel
import com.bekalmu.pos.ui.pengaturan.PengaturanViewModel
import com.bekalmu.pos.ui.transaksi.TransaksiViewModel

class ViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository) as T
            modelClass.isAssignableFrom(KasirViewModel::class.java) -> KasirViewModel(repository) as T
            modelClass.isAssignableFrom(InventoriViewModel::class.java) -> InventoriViewModel(repository) as T
            modelClass.isAssignableFrom(TransaksiViewModel::class.java) -> TransaksiViewModel(repository) as T
            modelClass.isAssignableFrom(KasViewModel::class.java) -> KasViewModel(repository) as T
            modelClass.isAssignableFrom(LaporanViewModel::class.java) -> LaporanViewModel(repository) as T
            modelClass.isAssignableFrom(PengaturanViewModel::class.java) -> PengaturanViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
