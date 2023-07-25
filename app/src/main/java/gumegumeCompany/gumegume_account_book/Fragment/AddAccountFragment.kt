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
    private var _binding: FragmentAddAccountBinding?= null
    private val binding get() = _binding

    private lateinit var accountType : String //수입 or 지출
    private lateinit var accountCategoryType:String //하위 카테고리

    private var incomeCnt = 0
    private var expensesCnt = 0

    //수입 하위 카테고리 버튼 목록
    private val incomeCategorySelectBtns = binding?.let {
        arrayListOf<TextView>(
        it.salarySelectBtn, it.allowanceSelectBtn
    )
    }
    //지출 하위 카테고리 버튼 목록
    private val expensesCategorySelectBtns = binding?.let {
        arrayListOf<TextView>(
        it.fixedExpensesSelectBtn, it.foodSelectBtn, it.dailyNecessitySelectBtn,
            it.giftSelectBtn, it.etcSelectBtn
    )
    }

    //하위 카테고리 선택 버튼용 OnCLickListener
    inner class SelectBtnListener : View.OnClickListener{
        override fun onClick(view: View?) {
            accountCategoryType = (view as Button).text.toString()
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
        var accountDateStr = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        binding?.accountDateBtn?.text  = accountDateStr
        //TODO: 커스텀 캘린터 일일 내역에서 추가하는 경우, 일일 내역의 날짜

        //날짜 버튼 -> Date Picker Dialog로 선택하기
        binding?.accountDateBtn?.setOnClickListener{
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener{view,year,month,day ->
                accountDateStr = "${year}/${month}/${day}"
                binding?.accountDateBtn?.text  = accountDateStr
            }
            DatePickerDialog(this.requireContext(), dateSetListener,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                .show()
        }

        //수입 버튼 -> 수입 하위 카테고리 보이기 (디폴트)
        binding?.incomeBtn?.setOnClickListener {

            // 수입 버튼 클릭 시 색상 변경
            binding!!.incomeBtn.isSelected = binding!!.incomeBtn.isSelected != true
            if(binding!!.incomeBtn.isSelected == true) binding!!.expensesBtn.isSelected = false

            accountType = "income"
            binding?.let { it1 -> showCategories(accountType, it1) }
        }
        //지출 버튼 -> 수입 하위 카테고리 보이기
        binding?.expensesBtn?.setOnClickListener {

            // 지출 버튼 클릭 시 색상 변경
            binding!!.expensesBtn.isSelected = binding!!.expensesBtn.isSelected != true
            if(binding!!.expensesBtn.isSelected == true) binding!!.incomeBtn.isSelected = false

            accountType = "expenses"
            binding?.let { it1 -> showCategories(accountType, it1) }
        }

        //하위 카테고리 onClickListener 지정
        if (incomeCategorySelectBtns != null) {
            for(btn in incomeCategorySelectBtns){
                btn.setOnClickListener(SelectBtnListener())
            }
        }
        if (expensesCategorySelectBtns != null) {
            for(btn in expensesCategorySelectBtns){
                btn.setOnClickListener(SelectBtnListener())
            }
        }

        //취소 버튼-> 홈 화면으로 돌아가기
        binding?.cancelBtn?.setOnClickListener {
            it.findNavController().navigate(R.id.action_add_account_to_home)
        }
        //추가 버튼-> 데이터베이스에 저장 -> 홈 화면으로 돌아가기
        binding?.addBtn?.setOnClickListener {
            val newAccountInfo = AccountInfo(
                binding!!.accountDateBtn.text.toString(),
                accountType,
                accountCategoryType,
                binding!!.accountTitleEdittext.text.toString(),
                binding!!.accountMemoEdittext.text.toString()
            )
            //TODO: 내역 데이터베이스에 저장

            it.findNavController().navigate(R.id.action_add_account_to_home)
        }

        return view
    }

    private fun showCategories(accountType:String, binding: FragmentAddAccountBinding){
        var incomeBtnVisibility:Boolean = when(accountType){
            "income" -> true
            "outcome" -> false
            else -> true
        }
        if (incomeCategorySelectBtns != null) {
            for(btn in incomeCategorySelectBtns){
                btn.isGone = incomeBtnVisibility
            }
        }
        if (expensesCategorySelectBtns != null) {
            for(btn in expensesCategorySelectBtns){
                btn.isGone = !incomeBtnVisibility
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}