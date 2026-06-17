package com.bekalmu.pos.ui.transaksi

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bekalmu.pos.data.db.BekalMuDatabase
import com.bekalmu.pos.data.model.TransaksiWithDetails
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.databinding.ActivityTransaksiBinding
import com.bekalmu.pos.utils.CurrencyUtils
import com.bekalmu.pos.utils.DateUtils
import com.bekalmu.pos.utils.ViewModelFactory
import com.google.android.material.chip.Chip
import java.util.Calendar

class TransaksiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransaksiBinding
    private lateinit var adapter: TransaksiAdapter

    private val viewModel: TransaksiViewModel by viewModels {
        val db = BekalMuDatabase.getDatabase(applicationContext)
        val repo = AppRepository(db.userDao(), db.produkDao(), db.transaksiDao(), db.kasDao())
        ViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        setupRecyclerView()
        setupMonthChips()
        loadTransaksi(0)
    }

    private fun setupRecyclerView() {
        adapter = TransaksiAdapter { dateMs ->
            // Klik header -> Detail Transaksi
            val intent = Intent(this, DetailTransaksiActivity::class.java)
            intent.putExtra("dateMs", dateMs)
            startActivity(intent)
        }
        binding.rvTransaksi.layoutManager = LinearLayoutManager(this)
        binding.rvTransaksi.adapter = adapter
    }

    private fun setupMonthChips() {
        binding.chipGroupBulan.removeAllViews()
        for (i in 0..2) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.MONTH, -i)
            val label = DateUtils.formatMonthYear(cal.timeInMillis)
            val chip = Chip(this).apply {
                text = label
                isCheckable = true
                isChecked = (i == 0)
                setChipBackgroundColorResource(com.bekalmu.pos.R.color.chip_color_selector)
                setTextColor(resources.getColorStateList(com.bekalmu.pos.R.color.chip_text_selector, null))
            }
            val offset = i
            chip.setOnClickListener { loadTransaksi(offset) }
            binding.chipGroupBulan.addView(chip)
        }
    }

    private fun loadTransaksi(offset: Int) {
        viewModel.getTransaksiByMonthOffset(offset).observe(this) { list ->
            val grouped = groupByDate(list)
            adapter.submitList(grouped)
        }
    }

    private fun groupByDate(list: List<TransaksiWithDetails>): List<TransaksiAdapter.ListItem> {
        val result = mutableListOf<TransaksiAdapter.ListItem>()
        val grouped = list.groupBy {
            val cal = Calendar.getInstance()
            cal.timeInMillis = it.transaksi.tanggal
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        }
        grouped.entries.sortedByDescending { it.key }.forEach { (dateMs, txList) ->
            val total = txList.filter { it.transaksi.status == "sukses" }.sumOf { it.transaksi.totalHarga }
            result.add(TransaksiAdapter.ListItem.Header(dateMs, txList.size, total))
        }
        return result
    }
}
