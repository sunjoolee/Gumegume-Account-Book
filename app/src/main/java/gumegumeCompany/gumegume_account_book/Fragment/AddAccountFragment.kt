package gumegumeCompany.gumegume_account_book.Fragment

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.navigation.findNavController
import gumegumeCompany.gumegume_account_book.AccountInfo.AccountInfo
import gumegumeCompany.gumegume_account_book.R
import gumegumeCompany.gumegume_account_book.MainActivity.Companion.categoryColorIds
import gumegumeCompany.gumegume_account_book.databinding.FragmentAddAccountBinding
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class AddAccountFragment : Fragment() {
    private var _binding: FragmentAddAccountBinding? = null
    private val binding get() = _binding

    private lateinit var accountType: String //수입 or 지출
    private lateinit var accountCategoryType: String //하위 카테고리

    private var incomeCnt = 0
    private var expensesCnt = 0

    //수입 하위 카테고리 버튼 목록
    private val incomeCategoryTextviewArray = binding?.let {
        arrayListOf<TextView>(
            it.salaryTextview,
            it.allowanceTextview
        )
    }

    //지출 하위 카테고리 버튼 목록
    private val expensesCategoryTextviewArray = binding?.let {
        arrayListOf<TextView>(
            it.fixedexpensesTextview,
            it.foodTextview,
            it.dailynecessityTextview,
            it.giftTextview,
            it.etcTextview
        )
    }

    //하위 카테고리 선택 버튼용 OnCLickListener
    inner class categoryTextviewOnClickListener : View.OnClickListener {
        override fun onClick(view: View?) {
            accountCategoryType = (view as Button).text.toString()

            if (incomeCategoryTextviewArray != null) {
                for(textView in incomeCategoryTextviewArray) {
                    textView.isSelected = false
                }
            }

            if (expensesCategoryTextviewArray != null) {
                for(textView in expensesCategoryTextviewArray) {
                    textView.isSelected = false
                }
            }

            view.isSelected = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAccountBinding.inflate(inflater, container, false)
        val view = binding?.root

        

        //TODO: 화면 회전시 입력 데이터값 유지

        //내역 날짜 디폴트 값 설정
        //홈 화면에서 추가하는 경우, 현재 날짜
        binding?.accountDateBtn?.text = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

        //TODO: day detail info fragment에서 추가하는 경우, 일일 내역의 날짜

        //Date Picker Dialog로 내역 날짜 선택하기
        binding?.accountDateBtn?.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                binding?.accountDateBtn?.text = "${year}/${month + 1}/${day}"
            }
            DatePickerDialog(
                this.requireContext(), dateSetListener,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //수입 버튼 -> 수입 하위 카테고리 보이기 (디폴트)
        binding?.incomeBtn?.setOnClickListener {
            //수입 버튼 클릭 시 색상 변경
            binding!!.incomeBtn.isSelected = !(binding!!.incomeBtn.isSelected)
            //수입 버튼과 지출 버튼 클릭 상태 늘 반대가 되도록 함
            binding!!.expensesBtn.isSelected = !(binding!!.incomeBtn.isSelected)

            accountType = "income"
            binding?.let { showCategories(accountType, it) }
        }

        //지출 버튼 -> 수입 하위 카테고리 보이기
        binding?.expensesBtn?.setOnClickListener {
            //지출 버튼 클릭 시 색상 변경
            binding!!.expensesBtn.isSelected = !(binding!!.expensesBtn.isSelected)
            //수입 버튼과 지출 버튼 클릭 상태 늘 반대가 되도록 함
            binding!!.incomeBtn.isSelected = !(binding!!.expensesBtn.isSelected)

            accountType = "expenses"
            binding?.let { showCategories(accountType, it) }
        }

        //하위 카테고리 onClickListener 지정
        incomeCategoryTextviewArray?.let{
            for (textview in it) {
                textview.setOnClickListener(categoryTextviewOnClickListener())
            }
        }

        expensesCategoryTextviewArray?.let{
            for (textview in it) {
                textview.setOnClickListener(categoryTextviewOnClickListener())
            }
        }

        binding?.AppBar?.setNavigationOnClickListener {
            view?.findNavController()?.navigate(R.id.action_add_accountFragment_to_homeFragment)
        }

        binding?.AppBar?.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.add_content -> {
                    // 데이터 베이스에 저장 후 HomeFragment로 이동
                }
                else -> {}
            }
            false
        }

        /*
        val newAccountInfo = AccountInfo(
            binding!!.accountDateBtn.text.toString(),
            accountType,
            accountCategoryType,
            binding!!.accountTitleEdittext.text.toString(),
            binding!!.accountContentEdittext.text.toString()
        )
        //TODO: 내역 데이터베이스에 저장
        */

        return view
    }

    private fun showCategories(accountType: String, binding: FragmentAddAccountBinding) {
        var incomeBtnVisibility: Boolean = when (accountType) {
            "income" -> true
            "outcome" -> false
            else -> true
        }
        incomeCategoryTextviewArray?.let{
            for (btn in it) {
                btn.isGone = incomeBtnVisibility
            }
        }
        expensesCategoryTextviewArray?.let{
            for (btn in it) {
                btn.isGone = !incomeBtnVisibility
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}