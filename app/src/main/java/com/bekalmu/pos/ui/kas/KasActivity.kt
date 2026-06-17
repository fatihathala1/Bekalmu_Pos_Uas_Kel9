package com.bekalmu.pos.ui.kas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bekalmu.pos.R
import com.bekalmu.pos.data.db.BekalMuDatabase
import com.bekalmu.pos.data.model.Kas
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.databinding.ActivityKasBinding
import com.bekalmu.pos.utils.CurrencyUtils
import com.bekalmu.pos.utils.SessionManager
import com.bekalmu.pos.utils.ViewModelFactory

class KasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKasBinding
    private lateinit var adapter: KasAdapter
    private lateinit var sessionManager: SessionManager

    private val viewModel: KasViewModel by viewModels {
        val db = BekalMuDatabase.getDatabase(applicationContext)
        val repo = AppRepository(db.userDao(), db.produkDao(), db.transaksiDao(), db.kasDao())
        ViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = KasAdapter(
            onDelete = { kas -> showDeleteDialog(kas) }
        )
        binding.rvKas.layoutManager = LinearLayoutManager(this)
        binding.rvKas.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.saldo.observe(this) { saldo ->
            binding.tvSaldo.text = CurrencyUtils.format(saldo ?: 0L)
        }

        viewModel.getKasBulanIni().observe(this) { kasList ->
            adapter.submitList(kasList)
            val masuk = kasList.filter { it.jenis == "masuk" }.sumOf { it.jumlah }
            val keluar = kasList.filter { it.jenis == "keluar" }.sumOf { it.jumlah }
            binding.tvTotalMasuk.text = CurrencyUtils.format(masuk)
            binding.tvTotalKeluar.text = CurrencyUtils.format(keluar)
        }

        viewModel.operationResult.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.clearResult()
            }
        }
    }

    private fun setupListeners() {
        binding.btnKasMasuk.setOnClickListener {
            showCatatKasDialog("masuk")
        }
        binding.btnKasKeluar.setOnClickListener {
            showCatatKasDialog("keluar")
        }
    }

    private fun showCatatKasDialog(jenis: String) {
        val intent = android.content.Intent(this, CatatKasActivity::class.java)
        intent.putExtra("jenis", jenis)
        startActivity(intent)
    }

    private fun showDeleteDialog(kas: Kas) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Catatan Kas")
            .setMessage("Yakin ingin menghapus catatan kas ${kas.kategori} sebesar ${CurrencyUtils.format(kas.jumlah)}?")
            .setPositiveButton("Hapus") { _, _ -> viewModel.deleteKas(kas) }
            .setNegativeButton("Batal", null)
            .show()
    }
}
