package gumegumeCompany.gumegume_account_book.DayDetailInfoFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import gumegumeCompany.gumegume_account_book.R
import gumegumeCompany.gumegume_account_book.databinding.FragmentDayDetailInfoBinding

class DayDetailInfoFragment : Fragment() {

    private val _binding by lazy { FragmentDayDetailInfoBinding.inflate(LayoutInflater.from(requireContext())) }
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = binding.root

        binding.run {

            // 뒤로 가기
            binding.AppBar.setNavigationOnClickListener {
                view.findNavController().navigate(R.id.action_dayDetailInfoFragment_to_homeFragment)
            }

            // menu 클릭 시 화면 이동
            AppBar.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.go_to_AddAccountFragment -> {
                        view.findNavController().navigate(R.id.action_dayDetailInfoFragment_to_add_accountFragment)
                    }
                    else -> {}
                }
                false
            }
        }

        return view
    }
}