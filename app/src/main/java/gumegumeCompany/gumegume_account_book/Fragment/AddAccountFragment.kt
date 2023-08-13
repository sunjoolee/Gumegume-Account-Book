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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
    private val binding get() = _binding!!

    //livedata
    private lateinit var model: AccountViewModel

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
        val view = binding.root

        // initialize accountInfo.value
        model = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        model.accountInfo.value = AccountInfo()
        Log.d(TAG, "onCreateView: initiate account info = ${model.accountInfo.value.toString()}")
        // AccountInfo(date=0000/00/00, type=수입, categoryType=월급, title=제목 없음, content=null)

        //accountInfo.value 값이 변경될 때마다 UI 업데이트
        val accountObserver = Observer<AccountInfo> { newAccountInfo ->
            Log.d(TAG, "accountObserver: account observer is called!")
            binding.apply{
                Log.d(TAG, "accountObserver: newAccountInfo.date = ${newAccountInfo.date}")
                accountDateBtn.text = newAccountInfo.date

                Log.d(TAG, "accountObserver: newAccountInfo.type = ${newAccountInfo.type}")
                changeTypeUI(newAccountInfo.type)

                Log.d(TAG, "accountObserver: newAccountInfo.categoryType = ${newAccountInfo.categoryType}")
                changeCategoryTypeUI(newAccountInfo.categoryType)

                Log.d(TAG, "accountObserver: newAccountInfo.title = ${newAccountInfo.title}")
                accountTitleEdittext.setText(newAccountInfo.title)
                Log.d(TAG, "accountObserver: newAccountInfo.content = ${newAccountInfo.content}")
                accountContentEdittext.setText(newAccountInfo.content)
            }
        }
        model.accountInfo.observe(viewLifecycleOwner, accountObserver)

        binding.run{
            // initialize textview arrays
            incomeCategoryTextviewArray.apply {
                add(salaryTextview)
                add(allowanceTextview)
            }
            expensesCategoryTextviewArray.apply {
                add(fixedexpensesTextview)
                add(transportTextview)
                add(foodTextview)
                add(dailynecessityTextview)
                add(giftTextview)
                add(etcTextview)
            }

            //화면 구성 요소들 onClickListener 연결
            accountDateBtn.setOnClickListener(DateButtonOnClickListener())
            incomeBtn.setOnClickListener(TypeOnClickListener())
            expensesBtn.setOnClickListener(TypeOnClickListener())
        }

        //내역 날짜 디폴트 설정
        //앱 바에서 추가하는 경우, 현재 날짜
        Log.d(TAG, "onCreateView: set date to default")
        model.accountInfo.value!!.date = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        //TODO: day detail info fragment에서 추가하는 경우, 일일 내역의 날짜

        //카테고리 & 하위 카테고리 디폴트 설정
        Log.d(TAG, "onCreateView: set type to default")
        changeTypeUI("수입")
        Log.d(TAG, "onCreateView: set category type to default")
        changeCategoryTypeUI("월급")

        for (textview in incomeCategoryTextviewArray) {
            textview.setOnClickListener(CategoryOnClickListener())
        }
        for (textview in expensesCategoryTextviewArray) {
            textview.setOnClickListener(CategoryOnClickListener())
        }

        //앱바 설정
        binding?.AppBar?.setNavigationOnClickListener {
            //뒤로 돌아갈 때 action 사용하지 않고 back stack에서 불러오기
            it.findNavController()?.popBackStack()
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

    private fun changeTypeUI(type: String) {
        Log.d(TAG, "changeTypeUI: type={$type}")

        if(type == "수입") {
            //수입 버튼 클릭 처리
            binding.incomeBtn.isSelected = true
            binding.expensesBtn.isSelected = false

            //수입 하위 카테고리 목록만 보여주기
            binding.incomeCategoryTextview.visibility = View.VISIBLE
            for (textview in incomeCategoryTextviewArray) {
                textview.visibility = View.VISIBLE
            }
            binding.expensesCategoryTextview.visibility = View.INVISIBLE
            for (textview in expensesCategoryTextviewArray) {
                textview.visibility = View.INVISIBLE
            }
        }else{
            //지출 버튼 클릭 처리
            binding.incomeBtn.isSelected = false
            binding.expensesBtn.isSelected = true

            //지출 하위 카테고리 목록만 보여주기
            binding.incomeCategoryTextview.visibility = View.INVISIBLE
            for (textview in incomeCategoryTextviewArray) {
                textview.visibility = View.INVISIBLE
            }
            binding.expensesCategoryTextview.visibility = View.VISIBLE
            for (textview in expensesCategoryTextviewArray) {
                textview.visibility = View.VISIBLE
            }
        }
    }
    private fun changeCategoryTypeUI(categoryType: String) {
        val type = model.accountInfo.value!!.type
        Log.d(TAG, "changeCategoryTypeUI: type={$type}, categoryType = ${categoryType}")

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
    inner class TypeOnClickListener : View.OnClickListener {
        override fun onClick(view: View?) {
            //타입 저장
            val type = (view as Button).text.toString()
            model.accountInfo.value!!.type = type

            Log.d(TAG, "TypeOnClickListener: type selected = ${type}")
            Log.d(TAG, "TypeOnCLickListener: accountInfo = ${model.accountInfo.value.toString()}")

            //타입 변경 시 하위 카테고리 디폴트 값으로 변경
            if(type=="수입"){
                model.accountInfo.value!!.categoryType = "월급"
            }else{
                model.accountInfo.value!!.categoryType = "고정지출"
            }

            //UI 변경은 Livedata Observer에서 changeTypeUI 함수 호출하여 처리
        }
    }

    //하위 카테고리 선택 버튼용 OnCLickListener
    inner class CategoryOnClickListener : View.OnClickListener {
        override fun onClick(view: View?) {
            //하위 카테고리 저장
            val categoryType = (view as TextView).text.toString()
            model.accountInfo.value!!.categoryType = categoryType

            Log.d(TAG, "CategoryOnClickListener: category selected = ${categoryType}")
            Log.d(TAG, "CategoryOnCLickListener: accountInfo = ${model.accountInfo.value.toString()}")

            //UI 변경은 Livedata Observer에서 changeCategoryUI 함수 호출하여 처리
        }
    }

    //날짜 선택용 OnCLickListener
    inner class DateButtonOnClickListener : View.OnClickListener {
        override fun onClick(view: View?) {
            Log.d(TAG, "DateButtonClickListener: initiate date picker dialog")

            //Date Picker Dialog로 내역 날짜 선택하기
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                Log.d(TAG, "DateButtonClickListener: picked date = ${year}/${month + 1}/${day}")
                model.accountInfo.value!!.date = "${year}/${month + 1}/${day}"
            }
            DatePickerDialog(
                context!!, dateSetListener,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}