package com.bekalmu.pos.ui.laporan

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bekalmu.pos.data.db.BekalMuDatabase
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.databinding.ActivityLaporanBinding
import com.bekalmu.pos.utils.CurrencyUtils
import com.bekalmu.pos.utils.SessionManager
import com.bekalmu.pos.utils.ViewModelFactory

class LaporanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaporanBinding
    private lateinit var sessionManager: SessionManager

    private val viewModel: LaporanViewModel by viewModels {
        val db = BekalMuDatabase.getDatabase(applicationContext)
        val repo = AppRepository(db.userDao(), db.produkDao(), db.transaksiDao(), db.kasDao())
        ViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        binding.btnBack.setOnClickListener { finish() }

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.pendapatanHariIni.observe(this) {
            binding.tvPendapatan.text = CurrencyUtils.format(it ?: 0L)
        }
        viewModel.labaBersih.observe(this) {
            binding.tvLabaBersih.text = CurrencyUtils.format(it ?: 0L)
        }
        // Tampilkan rekap kasir hanya untuk owner
        if (sessionManager.isOwner()) {
            binding.sectionRekapKasir.visibility = android.view.View.VISIBLE
            setupKasirAdapter()
        }
    }

    private fun setupKasirAdapter() {
        val adapter = RekapKasirAdapter()
        binding.rvRekapKasir.layoutManager = LinearLayoutManager(this)
        binding.rvRekapKasir.adapter = adapter
        viewModel.getRekapPerKasir().observe(this) { list ->
            adapter.submitList(list)
        }
    }
}
