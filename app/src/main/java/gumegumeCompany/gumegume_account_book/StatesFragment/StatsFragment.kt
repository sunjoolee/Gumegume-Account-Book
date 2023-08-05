package gumegumeCompany.gumegume_account_book.StatesFragment

import android.graphics.Color.BLACK
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import gumegumeCompany.gumegume_account_book.MainActivity.Companion.categoryColorIds
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

        //colors for pie chart
        val categoryColors = mutableListOf<Int>()
        for (id in categoryColorIds) {
            categoryColors.add(resources.getColor(id))
        }

        //sample entries for pie chart
        val sampleEntries: MutableList<PieEntry> = mutableListOf()
        sampleEntries.apply{
            add(PieEntry(600000f, "월급&용돈"))
            add(PieEntry(150000f, "고정지출"))
            add(PieEntry(200000f, "식비"))
            add(PieEntry(50000f, "교통비"))
            add(PieEntry(50000f, "생필품"))
            add(PieEntry(15000f, "선물"))
            add(PieEntry(12000f, "기타"))
        }

        val samplePieDataSet : PieDataSet =
            PieDataSet(sampleEntries, "통계 정보").apply {
            colors = categoryColors
            valueTextColor = BLACK
            valueTextSize = 10F
        }
        val samplePieData : PieData = PieData(samplePieDataSet)

        binding.pieChart.apply{
            data = samplePieData
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