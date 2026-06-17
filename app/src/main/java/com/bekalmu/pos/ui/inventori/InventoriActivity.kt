package com.bekalmu.pos.ui.inventori

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bekalmu.pos.R
import com.bekalmu.pos.data.db.BekalMuDatabase
import com.bekalmu.pos.data.model.Produk
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.databinding.ActivityInventoriBinding
import com.bekalmu.pos.utils.SessionManager
import com.bekalmu.pos.utils.ViewModelFactory
import com.google.android.material.chip.Chip

class InventoriActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInventoriBinding
    private lateinit var adapter: InventoriAdapter
    private lateinit var sessionManager: SessionManager

    private val viewModel: InventoriViewModel by viewModels {
        val db = BekalMuDatabase.getDatabase(applicationContext)
        val repo = AppRepository(db.userDao(), db.produkDao(), db.transaksiDao(), db.kasDao())
        ViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoriBinding.inflate(layoutInflater)
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
        adapter = InventoriAdapter(
            isOwner = sessionManager.isOwner(),
            onEdit = { produk -> showTambahEditDialog(produk) },
            onDelete = { produk -> showDeleteDialog(produk) },
            onRestok = { produk -> showRestokDialog(produk) }
        )
        binding.rvInventori.layoutManager = LinearLayoutManager(this)
        binding.rvInventori.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.totalStok.observe(this) { total ->
            binding.tvTotalStok.text = "${total ?: 0}"
        }
        viewModel.filteredProduk.observe(this) { produkList ->
            adapter.submitList(produkList)
        }
        viewModel.allProduk.observe(this) { produkList ->
            val kategoriList = produkList.map { it.kategori }.distinct()
            setupKategoriChips(kategoriList)
        }
        viewModel.operationResult.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.clearResult()
            }
        }
    }

    private fun setupKategoriChips(kategoriList: List<String>) {
        binding.chipGroupKategori.removeAllViews()

        fun buatChip(label: String, checked: Boolean): Chip {
            return Chip(this).apply {
                text = label
                isCheckable = true
                isChecked = checked
                isClickable = true
                chipBackgroundColor = resources.getColorStateList(
                    R.color.chip_bg_selector, theme
                )
                setTextColor(
                    resources.getColorStateList(R.color.chip_text_selector, theme)
                )
                chipStrokeWidth = 2f
                setChipStrokeColorResource(R.color.green_primary)
                chipCornerRadius = 40f
            }
        }

        val allChip = buatChip("Semua", true)
        allChip.setOnCheckedChangeListener { _, checked ->
            if (checked) viewModel.setKategori("Semua")
        }
        binding.chipGroupKategori.addView(allChip)

        kategoriList.forEach { kategori ->
            val chip = buatChip(kategori, false)
            chip.setOnCheckedChangeListener { _, checked ->
                if (checked) viewModel.setKategori(kategori)
            }
            binding.chipGroupKategori.addView(chip)
        }
    }

    private fun setupListeners() {
        // Tombol Tambah Produk: hanya owner
        if (sessionManager.isOwner()) {
            binding.btnTambahProduk.visibility = View.VISIBLE
            binding.btnTambahProduk.setOnClickListener {
                showTambahEditDialog(null)
            }
        } else {
            binding.btnTambahProduk.visibility = View.GONE
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setSearchQuery(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showTambahEditDialog(produk: Produk?) {
        val intent = android.content.Intent(this, TambahProdukActivity::class.java)
        produk?.let { intent.putExtra("produk", it) }
        startActivity(intent)
    }

    private fun showDeleteDialog(produk: Produk) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Produk")
            .setMessage("Yakin ingin menghapus \"${produk.nama}\"?")
            .setPositiveButton("Hapus") { _, _ -> viewModel.deleteProduk(produk) }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showRestokDialog(produk: Produk) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_restok, null)
        val etJumlah = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etJumlahRestok)
        AlertDialog.Builder(this)
            .setTitle("Tambah Stok: ${produk.nama}")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val jumlah = etJumlah.text.toString().toIntOrNull()
                if (jumlah != null && jumlah > 0) {
                    viewModel.restokProduk(produk.produkId, jumlah)
                } else {
                    Toast.makeText(this, "Jumlah tidak valid", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}