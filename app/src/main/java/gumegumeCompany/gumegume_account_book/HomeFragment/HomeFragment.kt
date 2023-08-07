package gumegumeCompany.gumegume_account_book.HomeFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.shape.MaterialShapeDrawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import gumegumeCompany.gumegume_account_book.Calendar.TodayDecorator
import gumegumeCompany.gumegume_account_book.R
import gumegumeCompany.gumegume_account_book.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    //tag for logging
    private val TAG = "HomeFragment"

    private val _binding by lazy { FragmentHomeBinding.inflate(LayoutInflater.from(requireContext())) }
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = binding.root

        binding.run {

            calendar.selectedDate = CalendarDay.today()
            calendar.addDecorator(TodayDecorator())

            // 상단 앱 바의 배경색이 보일 수 있는 투명한 상태 표시줄이 나타납니다
            AppbarLayout.setStatusBarForeground(
                MaterialShapeDrawable.createWithElevationOverlay(getContext()))

            // menu 클릭 시 화면 이동
            AppBar.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.go_to_AddAccountFragment -> {
                        view.findNavController().navigate(R.id.action_home_to_add_account)
                    }
                    else -> {}
                }
                false
            }
        }
        return view
    }
}