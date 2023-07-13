package gumegumeCompany.gumegume_account_book.Fragment

import android.graphics.Color.BLACK
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import gumegumeCompany.gumegume_account_book.R
import gumegumeCompany.gumegume_account_book.databinding.FragmentStatsBinding

class StatsFragment : Fragment() {
    private var _binding: FragmentStatsBinding?= null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val view = binding.root

        //colors for pie charrt
        val pie_chart_colors = arrayListOf(
            resources.getColor(R.color.pastel_rainbow1),
            resources.getColor(R.color.pastel_rainbow2),
            resources.getColor(R.color.pastel_rainbow3),
            resources.getColor(R.color.pastel_rainbow4),
            resources.getColor(R.color.pastel_rainbow5),
            resources.getColor(R.color.pastel_rainbow6),
            resources.getColor(R.color.pastel_rainbow7))

        //sample entries for pie chart
        val sample_entries: MutableList<PieEntry> = mutableListOf()
        sample_entries.add(PieEntry(600000f, "월급&용돈"))
        sample_entries.add(PieEntry(150000f, "고정지출"))
        sample_entries.add(PieEntry(200000f, "식비"))
        sample_entries.add(PieEntry(50000f, "교통비"))
        sample_entries.add(PieEntry(50000f, "생필품"))
        sample_entries.add(PieEntry(15000f, "선물"))
        sample_entries.add(PieEntry(12000f, "기타"))

        val sample_pieDataSet : PieDataSet =
            PieDataSet(sample_entries, "통계 정보").apply {
            colors = pie_chart_colors
            valueTextColor = BLACK
            valueTextSize = 10F
        }
        val sample_pieData : PieData = PieData(sample_pieDataSet)

        binding.pieChart.apply{
            data = sample_pieData
            description.isEnabled = false
            centerText="7월 통계"
            //setCenterTextTypeface(Typeface.createFromAsset(requireActivity().assets, "font/humanbumsuk.ttf"))
            //description.typeface = Typeface.createFromAsset(requireActivity().assets, "font/humanbumsuk.ttf")
        }.animate()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}