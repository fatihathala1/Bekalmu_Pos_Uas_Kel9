package com.bekalmu.pos.ui.laporan

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bekalmu.pos.R
import com.bekalmu.pos.data.db.BekalMuDatabase
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.databinding.FragmentLaporanBinding
import com.bekalmu.pos.utils.CurrencyUtils
import com.bekalmu.pos.utils.SessionManager
import com.bekalmu.pos.utils.ViewModelFactory
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class LaporanFragment : Fragment() {

    private var _binding: FragmentLaporanBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    private val viewModel: LaporanViewModel by viewModels {
        val db = BekalMuDatabase.getDatabase(requireContext())
        val repo = AppRepository(db.userDao(), db.produkDao(), db.transaksiDao(), db.kasDao())
        ViewModelFactory(repo)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLaporanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        binding.tvCabang.text = "Cabang: ${sessionManager.getKodeCabang()}"
        setupChart()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.pendapatanHariIni.observe(viewLifecycleOwner) { pendapatan ->
            binding.tvPendapatan.text = CurrencyUtils.format(pendapatan ?: 0L)
        }
        viewModel.labaBersih.observe(viewLifecycleOwner) { laba ->
            binding.tvLabaBersih.text = CurrencyUtils.format(laba ?: 0L)
            val pendapatan = viewModel.pendapatanHariIni.value ?: 1L
            val margin = if (pendapatan > 0L && laba != null && laba > 0)
                ((laba.toFloat() / pendapatan) * 100).toInt() else 0
            binding.tvMarginKeuntungan.text = "Margin keuntungan $margin%"
        }
        viewModel.weeklyData.observe(viewLifecycleOwner) { data -> updateChart(data) }
    }

    private fun setupChart() {
        binding.lineChart.apply {
            setBackgroundColor(Color.TRANSPARENT)
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(false)
            setPinchZoom(false)
            axisRight.isEnabled = false
            legend.isEnabled = false
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textColor = Color.parseColor("#666666")
                granularity = 1f
            }
            axisLeft.apply {
                setDrawGridLines(true)
                gridColor = Color.parseColor("#EEEEEE")
                textColor = Color.parseColor("#666666")
                axisMinimum = 0f
            }
        }
    }

    private fun updateChart(data: List<Pair<String, Long>>) {
        val entries = data.mapIndexed { i, pair -> Entry(i.toFloat(), pair.second.toFloat()) }
        val labels = data.map { it.first }
        val dataSet = LineDataSet(entries, "Pendapatan").apply {
            color = requireContext().getColor(R.color.green_primary)
            setCircleColor(requireContext().getColor(R.color.green_primary))
            lineWidth = 2.5f
            circleRadius = 5f
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            fillDrawable = requireContext().getDrawable(R.drawable.chart_gradient_fill)
            setDrawFilled(true)
        }
        binding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.lineChart.data = LineData(dataSet)
        binding.lineChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
