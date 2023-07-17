package gumegumeCompany.gumegume_account_book.Fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.kakao.sdk.user.UserApiClient
import gumegumeCompany.gumegume_account_book.LoginActivity
import gumegumeCompany.gumegume_account_book.R
import gumegumeCompany.gumegume_account_book.databinding.FragmentSettingsBinding
import java.time.LocalDate


class SettingsFragment : Fragment() {
    //tag for logging
    private val TAG = "SettingsFragment"

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        //현재 로그인한 사용자 정보 프로필에 표시
        var userName: String = "비회원"
        var userGender: String = "unKnown"
        var userBirthYear: String = "0000";
        var userBirthDate: String = "00/00";
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성공")
                binding.run {
                    profileUserName.text = "${user.kakaoAccount?.profile?.nickname}"

                    when (user.kakaoAccount?.gender.toString()) {
                        "FEMALE" -> profileUserGender.setImageDrawable(resources.getDrawable(R.drawable.venus_18))
                        "MALE" -> profileUserGender.setImageDrawable(resources.getDrawable(R.drawable.mars_18))
                        else -> profileUserGender.setImageDrawable(resources.getDrawable(R.drawable.money_18))
                    }
                    profileUserBirth.text = "=${user?.kakaoAccount?.birthday}"
                }

                //userNickname = user.kakaoAccount?.profile?.nickname.toString()

                //userGender = user.kakaoAccount?.gender.toString()
                //userBirthYear = user.kakaoAccount?.birthyear.toString()
                //userBirthDate = user.kakaoAccount?.legalBirthDate.toString()
            }
        }
//        binding.profileUserName.text = resources.getString(R.string.user_name, userName)
        /*        binding.profileUserGender.apply{
                    when(userGender){
                        "unKnown" -> isGone = true
                        "male" -> { isGone = false
                            setImageDrawable(resources.getDrawable(R.drawable.mars_18)) }
                        "female" -> { isGone = false
                            setImageDrawable(resources.getDrawable(R.drawable.venus_18)) }
                    }
                }*/
//        binding.profileUserBirth.text = resources.getString(R.string.user_birth, userBirthYear, userBirthDate)

        binding.logoutBtn.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    activity?.let{
                        Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }
            }
        }

        //오늘의 금전운
        //사용자 정보 입력받기
        binding.financeLuckGenderBtn.setOnClickListener {
            val woman = resources.getString(R.string.finance_luck_woman)
            val man = resources.getString(R.string.finance_luck_man)

            val gender = (it as Button).text
            (it as AppCompatButton).text = if (gender == woman) man else woman
        }
        binding.financeLuckBirthdateBtn.setOnClickListener {
            startBirthdateDialog(it as Button)
        }

        //문의하기
        binding.contactChatbotBtn.setOnClickListener {
            //TODO: implement chatbot
        }

        binding.contactEmailBtn.setOnClickListener {
            val developerEmails: Array<String> =
                arrayOf("sheisthepoem@naver.com", "kimsw215@naver.com")

            val contactEmailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "*/*"
                putExtra(Intent.EXTRA_EMAIL, developerEmails)
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_title))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.contact_body))
            }
            if (contactEmailIntent.resolveActivity(requireActivity().packageManager) != null) {
                Log.d(TAG, "start contact email intent")
                startActivity(contactEmailIntent)
            } else {
                Log.d(TAG, "could not start contact email intent")
            }
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startBirthdateDialog(birthdateBtn:Button){
        val layout = layoutInflater.inflate(R.layout.dialog_birthdate_select, null)
        val build = AlertDialog.Builder(context).setView(layout)
        val dialog = build.create()
        dialog.show()

        val numberPickerYear = layout.findViewById<NumberPicker>(R.id.number_picker_year)
            .apply{
                minValue = LocalDate.now().year - 100
                maxValue = LocalDate.now().year
            }
        val numberPickerMonth = layout.findViewById<NumberPicker>(R.id.number_picker_month)
            .apply{
                minValue = 1
                maxValue = 12
            }
        val numberPickerDay = layout.findViewById<NumberPicker>(R.id.number_picker_day)
            .apply{
                minValue = 1
                maxValue = 31
            }

        var selectedYear = 0
        var selectedMonth = 0
        var selectedDay = 0

        layout.findViewById<Button>(R.id.number_picker_cancel_btn).setOnClickListener {
            Log.d(TAG, "number picker dialog cancel")
            dialog.dismiss()
        }
        layout.findViewById<Button>(R.id.number_picker_ok_btn).setOnClickListener {
            selectedYear = numberPickerYear.value
            selectedMonth = numberPickerMonth.value
            selectedDay = numberPickerDay.value

            birthdateBtn.text = "${selectedYear}년 ${selectedMonth}월 ${selectedDay}일"
            Log.d(TAG, "number picker dialog ok")
            dialog.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}