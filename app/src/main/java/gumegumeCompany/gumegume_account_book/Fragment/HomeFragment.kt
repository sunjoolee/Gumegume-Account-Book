package gumegumeCompany.gumegume_account_book.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.prolificinteractive.materialcalendarview.CalendarDay
import gumegumeCompany.gumegume_account_book.Calendar.TodayDecorator
import gumegumeCompany.gumegume_account_book.R
import gumegumeCompany.gumegume_account_book.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding?= null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.run {

            calendar.setSelectedDate(CalendarDay.today())
            calendar.addDecorator(TodayDecorator())

            // 플로팅 버튼 클릭 시
            addAccountFloatingBtn.setOnClickListener {
                view?.findNavController()?.navigate(R.id.action_home_to_add_account)
            }
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}