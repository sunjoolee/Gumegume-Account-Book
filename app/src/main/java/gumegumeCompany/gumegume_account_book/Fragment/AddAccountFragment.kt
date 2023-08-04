package gumegumeCompany.gumegume_account_book.Fragment

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import gumegumeCompany.gumegume_account_book.AccountData.AccountInfo
import gumegumeCompany.gumegume_account_book.AccountData.AccountViewModel
import gumegumeCompany.gumegume_account_book.R
import gumegumeCompany.gumegume_account_book.databinding.FragmentAddAccountBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class AddAccountFragment : Fragment() {
    //tag for logging
    private val TAG = "AddAccountFragment"

    private var _binding: FragmentAddAccountBinding? = null
    private val binding get() = _binding

    private val model:AccountViewModel by viewModels()

    //수입 하위 카테고리 TextView 목록
    private val incomeCategoryTextviewArray = binding?.let {
        arrayListOf<TextView>(
            it.salaryTextview,
            it.allowanceTextview
        )
    }
    //지출 하위 카테고리 TextView 목록
    private val expensesCategoryTextviewArray = binding?.let {
        arrayListOf<TextView>(
            it.fixedexpensesTextview,
            it.foodTextview,
            it.dailynecessityTextview,
            it.giftTextview,
            it.etcTextview
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAccountBinding.inflate(inflater, container, false)
        val view = binding?.root

        //AccountInfo 값이 변경될 때마다 UI 업데이트
        val accountObserver = Observer<AccountInfo>{ newAccountInfo ->
            Log.d(TAG, newAccountInfo.toString())

            binding?.let{
                it.accountDateTextview?.text = newAccountInfo.date
                showCategories(newAccountInfo.type, it) //타입에 맞는 하위 카테고리 보이기&숨기기
                it.accountTitleEdittext?.setText(newAccountInfo.title)
                it.accountContentEdittext?.setText(newAccountInfo.content)
            }
        }
        model.accountInfo.observe(viewLifecycleOwner, accountObserver)

        //내역 날짜 디폴트 값 설정
        //앱 바에서 추가하는 경우, 현재 날짜
        model.accountInfo.value?.date = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        //TODO: day detail info fragment에서 추가하는 경우, 일일 내역의 날짜

        binding?.accountDateBtn?.setOnClickListener (dateButtonOnClickListener())

        binding?.incomeBtn?.setOnClickListener(typeOnClickListener())
        binding?.expensesBtn?.setOnClickListener(typeOnClickListener())

        incomeCategoryTextviewArray?.let{
            for (textview in it) {
                textview.setOnClickListener(categoryOnClickListener())
            }
        }
        expensesCategoryTextviewArray?.let{
            for (textview in it) {
                textview.setOnClickListener(categoryOnClickListener())
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
        //추가 버튼-> 데이터베이스에 저장 -> 홈 화면으로 돌아가기
        binding?.addAccountBtn?.setOnClickListener {
            //TODO: 내역 데이터베이스에 저장

            //뒤로 돌아갈 때 action 사용하지 않고 back stack에서 불러오기
            it.findNavController()?.popBackStack()
        }

        return view
    }

    private fun showCategories(accountType: String, binding: FragmentAddAccountBinding) {
        //현재 type 수입인 경우 수입 하위 카테고리 보여주기
        //현재 type 지출인 경우 지출 하위 카테고리 보여주기
        var incomeVisibility: Boolean = when (accountType) {
            resources.getString(R.string.income) -> true
            resources.getString(R.string.expenses) -> false
            else -> true
        }
        incomeCategoryTextviewArray?.let{
            for (btn in it) { btn.isGone = incomeVisibility }
        }
        expensesCategoryTextviewArray?.let{
            for (btn in it) { btn.isGone = !incomeVisibility }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 타입(수입/지출) 선택용 OnClickListener
    inner class typeOnClickListener : View.OnClickListener {
        override fun onClick(view: View?) {
            //타입 저장
            val type = (view as Button).text.toString()
            model.accountInfo.value?.type = type

            //버튼 selected 상태 toggle
            view.isSelected = !(view.isSelected)

            //수입 버튼과 지출 버튼 클릭 상태 늘 반대가 되도록 함
            if(type == resources.getString(R.string.income)) {
                binding!!.expensesBtn.isSelected = !(view.isSelected)
            }
            else {
                binding!!.incomeBtn.isSelected = !(view.isSelected)
            }
        }
    }
    //하위 카테고리 선택 버튼용 OnCLickListener
    inner class categoryOnClickListener : View.OnClickListener {
        override fun onClick(view: View?) {
            //하위 카테고리 저장
            model.accountInfo.value?.categoryType = (view as Button).text.toString()

            incomeCategoryTextviewArray?.let {
                for (textview in it) { textview.isSelected = false }
            }
            expensesCategoryTextviewArray?.let {
                for (textview in it) { textview.isSelected = false }
            }
            view.isSelected = true
        }
    }
    //날짜 선택용 OnCLickListener
    inner class dateButtonOnClickListener : View.OnClickListener {
        override fun onClick(view: View?) {
            //Date Picker Dialog로 내역 날짜 선택하기
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                model.accountInfo.value?.date = "${year}/${month + 1}/${day}"
            }
            DatePickerDialog(
                context!!, dateSetListener,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}