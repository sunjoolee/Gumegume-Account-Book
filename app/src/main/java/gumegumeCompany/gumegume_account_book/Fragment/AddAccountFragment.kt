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
import androidx.core.view.isVisible
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

    //livedata
    private val model: AccountViewModel by viewModels()

    //수입 하위 카테고리 TextView 목록
    private val incomeCategoryTextviewArray = mutableListOf<TextView>()

    //지출 하위 카테고리 TextView 목록
    private val expensesCategoryTextviewArray = mutableListOf<TextView>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAccountBinding.inflate(inflater, container, false)
        val view = binding?.root

        // initialize textview arrays
        binding?.let {
            incomeCategoryTextviewArray.apply {
                add(it.salaryTextview)
                add(it.allowanceTextview)
            }
            expensesCategoryTextviewArray.apply {
                add(it.fixedexpensesTextview)
                add(it.transportTextview)
                add(it.foodTextview)
                add(it.dailynecessityTextview)
                add(it.giftTextview)
                add(it.etcTextview)
            }
        }

        Log.d(TAG, model.accountInfo.value.toString()) //null
        // initialize accountInfo.value
        // AccountInfo(date=0000/00/00, type=수입, categoryType=월급, title=제목 없음, content=null)
        model.accountInfo.value = AccountInfo()
        Log.d(TAG, model.accountInfo.value.toString())

        //내역 날짜 디폴트 설정
        //앱 바에서 추가하는 경우, 현재 날짜
        model.accountInfo.value?.date = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        //TODO: day detail info fragment에서 추가하는 경우, 일일 내역의 날짜

        //카테고리 & 하위 카테고리 디폴트 설정
        changeTypeUI("수입")
        changeCategoryTypeUI("월급")

        //화면 구성 요소들 onClickListener 연결
        binding?.accountDateBtn?.setOnClickListener(dateButtonOnClickListener())

        binding?.incomeBtn?.setOnClickListener(typeOnClickListener())
        binding?.expensesBtn?.setOnClickListener(typeOnClickListener())

        for (textview in incomeCategoryTextviewArray) {
            textview.setOnClickListener(categoryOnClickListener())
        }
        for (textview in expensesCategoryTextviewArray) {
            textview.setOnClickListener(categoryOnClickListener())
        }

        //앱바 설정
        binding?.AppBar?.setNavigationOnClickListener {
            view?.findNavController()?.navigate(R.id.action_add_accountFragment_to_homeFragment)
        }

        binding?.AppBar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_content -> {
                    // 데이터 베이스에 저장 후 HomeFragment로 이동
                }

                else -> {}
            }
            false
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //AccountInfo 값이 변경될 때마다 UI 업데이트
        val accountObserver = Observer<AccountInfo> { newAccountInfo ->
            Log.d(TAG, "account observer: ${newAccountInfo.toString()}")

            binding?.let {
                it.accountDateBtn?.text = newAccountInfo.date
                changeTypeUI(newAccountInfo.type)
                changeCategoryTypeUI(newAccountInfo.categoryType)
                it.accountTitleEdittext?.setText(newAccountInfo.title)
                it.accountContentEdittext?.setText(newAccountInfo.content)
            }
        }
        model.accountInfo.observe(viewLifecycleOwner, accountObserver)
    }

    private fun changeTypeUI(type: String) {

        Log.d(TAG, "changeTypeUI: type = ${type}")

        if(type == "수입") {
            //수입 버튼 클릭 처리
            binding?.incomeBtn?.isSelected = true
            binding?.expensesBtn?.isSelected = false

            //수입 하위 카테고리 목록만 보여주기
            binding?.incomeCategoryTextview?.visibility = View.VISIBLE
            for (textview in incomeCategoryTextviewArray) {
                textview.visibility = View.VISIBLE
            }
            binding?.expensesCategoryTextview?.visibility = View.INVISIBLE
            for (textview in expensesCategoryTextviewArray) {
                textview.visibility = View.INVISIBLE
            }
        }else{
            //지출 버튼 클릭 처리
            binding?.incomeBtn?.isSelected = false
            binding?.expensesBtn?.isSelected = true

            //지출 하위 카테고리 목록만 보여주기
            binding?.incomeCategoryTextview?.visibility = View.INVISIBLE
            for (textview in incomeCategoryTextviewArray) {
                textview.visibility = View.INVISIBLE
            }
            binding?.expensesCategoryTextview?.visibility = View.VISIBLE
            for (textview in expensesCategoryTextviewArray) {
                textview.visibility = View.VISIBLE
            }
        }
    }
    private fun changeCategoryTypeUI(categoryType: String) {
        val type = model.accountInfo.value?.type

        Log.d(TAG, "changeCategoryTypeUI: type = ${type}")
        Log.d(TAG, "changeCategoryTypeUI: categoryType = ${categoryType}")

        //클릭된 하위 카테고리만 selected 되기
        if (type == "수입") {
            for (textview in incomeCategoryTextviewArray) {
                //모든 하위 카테고리 isSelected = false
                textview.isSelected = false
                //클릭된 하위 카테고리 isSelected = true
                if (textview.text.toString() == categoryType) {
                    textview.isSelected = true
                }
            }
        } else {
            for (textview in expensesCategoryTextviewArray) {
                //모든 하위 카테고리 isSelected = false
                textview.isSelected = false
                //클릭된 하위 카테고리 isSelected = true
                if (textview.text.toString() == categoryType) {
                    textview.isSelected = true
                }
            }
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

            Log.d(TAG, "typeOnClickListener: type selected = ${type}")

            //타입 변경 시 하위 카테고리 디폴트 값으로 변경
            if(type=="수입"){
                model.accountInfo.value?.categoryType = "월급"
            }else{
                model.accountInfo.value?.categoryType = "고정지출"
            }

            //UI 변경은 Livedata Observer에서 changeTypeUI 함수 호출하여 처리
        }
    }

    //하위 카테고리 선택 버튼용 OnCLickListener
    inner class categoryOnClickListener : View.OnClickListener {
        override fun onClick(view: View?) {
            //하위 카테고리 저장
            val categoryType = (view as TextView).text.toString()
            model.accountInfo.value?.categoryType = categoryType

            Log.d(TAG, "categoryOnClickListener: category selected = ${categoryType}")

            //UI 변경은 Livedata Observer에서 changeCategoryUI 함수 호출하여 처리
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