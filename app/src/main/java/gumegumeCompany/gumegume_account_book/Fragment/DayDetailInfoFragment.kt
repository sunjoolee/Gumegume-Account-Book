package gumegumeCompany.gumegume_account_book.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gumegumeCompany.gumegume_account_book.databinding.FragmentDayDetailInfoBinding


class DayDetailInfoFragment : Fragment() {

    private var _binding: FragmentDayDetailInfoBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDayDetailInfoBinding.inflate(inflater,container,false)
        val view = binding.root



        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}