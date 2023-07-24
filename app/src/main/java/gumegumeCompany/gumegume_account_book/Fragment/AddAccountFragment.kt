package gumegumeCompany.gumegume_account_book.Fragment

import android.app.Activity
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
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isGone
import androidx.navigation.findNavController
import gumegumeCompany.gumegume_account_book.AccountInfo.AccountInfo
import gumegumeCompany.gumegume_account_book.MainActivity
import gumegumeCompany.gumegume_account_book.databinding.ActivityMainBinding
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
    private val binding get() = _binding!!

    private lateinit var accountType : String //수입 or 지출
    private lateinit var accountCategoryType:String //하위 카테고리

    //수입 하위 카테고리 버튼 목록
    private lateinit var incomeCategorySelectBtns : MutableList<TextView>
    //지출 하위 카테고리 버튼 목록
    private lateinit var expensesCategorySelectBtns : MutableList<TextView>

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
        val view = binding.root

        //수입 하위 카테고리 버튼 목록
        incomeCategorySelectBtns = mutableListOf<TextView>(
            binding.salarySelectBtn, binding.allowanceSelectBtn
        )
        //지출 하위 카테고리 버튼 목록
        expensesCategorySelectBtns = mutableListOf<TextView>(
            binding.fixedExpensesSelectBtn, binding.foodSelectBtn, binding.dailyNecessitySelectBtn,
            binding.giftSelectBtn, binding.etcSelectBtn
        )

        //TODO: 화면 회전시 입력 데이터값 유지

        //내역 날짜 디폴트 값 설정
        //홈 화면에서 추가하는 경우, 현재 날짜
        var accountDateStr = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        binding.accountDateBtn.text = accountDateStr
        //TODO: 커스텀 캘린터 일일 내역에서 추가하는 경우, 일일 내역의 날짜

        //날짜 버튼 -> Date Picker Dialog로 선택하기
        binding.accountDateBtn.setOnClickListener{
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener{view,year,month,day ->
                accountDateStr = "${year}/${month}/${day}"
                binding.accountDateBtn.text = accountDateStr
            }
            DatePickerDialog(this.requireContext(), dateSetListener,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                .show()
        }

        //수입 버튼 -> 수입 하위 카테고리 보이기 (디폴트)
        binding.incomeBtn.setOnClickListener {
            accountType = "income"
            showCategories(accountType,binding)
        }
        //지출 버튼 -> 수입 하위 카테고리 보이기
        binding.expensesBtn.setOnClickListener {
            accountType = "expenses"
            showCategories(accountType, binding)
        }

        //하위 카테고리 onClickListener 지정
        for(btn in incomeCategorySelectBtns){
            btn.setOnClickListener(SelectBtnListener())
        }
        for(btn in expensesCategorySelectBtns){
            btn.setOnClickListener(SelectBtnListener())
        }

        //취소 버튼-> 홈 화면으로 돌아가기
        binding.cancelBtn.setOnClickListener {
            //뒤로 돌아갈 때 action 사용하지 않고 back stack에서 불러오기
            it.findNavController()?.popBackStack()
        }
        //추가 버튼-> 데이터베이스에 저장 -> 홈 화면으로 돌아가기
        binding.addBtn.setOnClickListener {
            val newAccountInfo = AccountInfo(
                binding.accountDateBtn.text.toString(),
                accountType,
                accountCategoryType,
                binding.accountTitleEdittext.text.toString(),
                binding.accountMemoEdittext.text.toString()
            )
            //TODO: 내역 데이터베이스에 저장

            //뒤로 돌아갈 때 action 사용하지 않고 back stack에서 불러오기
            it.findNavController()?.popBackStack()
        }

        return view
    }

    private fun showCategories(accountType:String, binding: FragmentAddAccountBinding){
        var incomeBtnVisibility:Boolean = when(accountType){
            "income" -> true
            "outcome" -> false
            else -> true
        }
        for(btn in incomeCategorySelectBtns){
            btn.isGone = incomeBtnVisibility
        }
        for(btn in expensesCategorySelectBtns){
            btn.isGone = !incomeBtnVisibility
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}