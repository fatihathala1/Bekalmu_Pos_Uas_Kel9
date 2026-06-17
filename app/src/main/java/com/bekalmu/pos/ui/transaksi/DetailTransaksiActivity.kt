package com.bekalmu.pos.ui.transaksi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bekalmu.pos.data.db.BekalMuDatabase
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.databinding.ActivityDetailTransaksiBinding
import com.bekalmu.pos.utils.CurrencyUtils
import com.bekalmu.pos.utils.DateUtils
import com.bekalmu.pos.utils.ViewModelFactory

class DetailTransaksiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTransaksiBinding
    private lateinit var adapter: DetailTransaksiAdapter

    private val viewModel: TransaksiViewModel by viewModels {
        val db = BekalMuDatabase.getDatabase(applicationContext)
        val repo = AppRepository(db.userDao(), db.produkDao(), db.transaksiDao(), db.kasDao())
        ViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dateMs = intent.getLongExtra("dateMs", System.currentTimeMillis())
        binding.tvTanggal.text = "Detail Transaksi (${DateUtils.formatDate(dateMs)})"

        binding.btnBack.setOnClickListener { finish() }

        setupRecyclerView()
        observeTransaksi(dateMs)
        setupSearch()
    }

    private fun setupRecyclerView() {
        adapter = DetailTransaksiAdapter()
        binding.rvTransaksi.layoutManager = LinearLayoutManager(this)
        binding.rvTransaksi.adapter = adapter
    }

    private fun observeTransaksi(dateMs: Long) {
        viewModel.getTransaksiByDate(dateMs).observe(this) { list ->
            val total = list.filter { it.transaksi.status == "sukses" }.sumOf { it.transaksi.totalHarga }
            val count = list.filter { it.transaksi.status == "sukses" }.size
            binding.tvTotalPendapatan.text = CurrencyUtils.format(total)
            binding.tvJumlahTransaksi.text = "$count Transaksi Berhasil"
            adapter.submitFullList(list)
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filter(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
