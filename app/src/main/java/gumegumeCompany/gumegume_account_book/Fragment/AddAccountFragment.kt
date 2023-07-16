package gumegumeCompany.gumegume_account_book.Fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gumegumeCompany.gumegume_account_book.MainActivity
import gumegumeCompany.gumegume_account_book.databinding.ActivityMainBinding
import gumegumeCompany.gumegume_account_book.R
import gumegumeCompany.gumegume_account_book.MainActivity.Companion.categoryColorIds
import gumegumeCompany.gumegume_account_book.databinding.FragmentAddAccountBinding

class AddAccountFragment : Fragment() {
    private var _binding: FragmentAddAccountBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAccountBinding.inflate(inflater, container, false)
        val view = binding.root

        val categoryColors = mutableListOf<Int>()
        for(id in categoryColorIds){
            categoryColors.add(resources.getColor(id))
        }
        
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}