package gumegumeCompany.gumegume_account_book.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import gumegumeCompany.gumegume_account_book.R
import gumegumeCompany.gumegume_account_book.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    //tag for logging
    private val TAG = "HomeFragment"

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

        binding.addAccountFloatingBtn.setOnClickListener { view ->
            Log.d(TAG, "add account floating btn clicked")
            view?.findNavController()?.navigate(R.id.action_home_to_add_account)
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}