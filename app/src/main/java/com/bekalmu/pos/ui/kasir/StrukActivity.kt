package com.bekalmu.pos.ui.kasir

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bekalmu.pos.databinding.ActivityStrukBinding
import com.bekalmu.pos.ui.laporan.LaporanActivity
import com.bekalmu.pos.utils.CurrencyUtils
import com.bekalmu.pos.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class StrukActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStrukBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStrukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nomorOrder = intent.getStringExtra("nomorOrder") ?: "-"
        val totalBayar = intent.getLongExtra("totalBayar", 0L)
        val metode = intent.getStringExtra("metode") ?: "Tunai"
        val namaKasir = intent.getStringExtra("namaKasir") ?: "-"

        binding.tvNomorOrder.text = "#$nomorOrder"
        binding.tvTotalBayar.text = CurrencyUtils.format(totalBayar)
        binding.tvMetode.text = metode.replaceFirstChar { it.uppercase() }
        binding.tvNamaKasir.text = namaKasir

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.tvWaktu.text = "Hari ini, ${sdf.format(Date())}"

        binding.btnCetakStruk.setOnClickListener {
            showNotifStruk()
        }

        binding.btnPesananBaru.setOnClickListener {
            finish() // Kembali ke KasirFragment → cart baru
        }

        binding.btnDismissNotif.setOnClickListener {
            binding.notifStruk.visibility = View.GONE
        }

        binding.btnLihatRekap.setOnClickListener {
            startActivity(Intent(this, LaporanActivity::class.java))
        }
    }

    private fun showNotifStruk() {
        binding.notifStruk.visibility = View.VISIBLE
        // Auto hide setelah 4 detik
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isDestroyed) {
                binding.notifStruk.animate()
                    .alpha(0f)
                    .setDuration(500)
                    .withEndAction {
                        binding.notifStruk.visibility = View.GONE
                        binding.notifStruk.alpha = 1f
                    }.start()
            }
        }, 4000)
    }
}
