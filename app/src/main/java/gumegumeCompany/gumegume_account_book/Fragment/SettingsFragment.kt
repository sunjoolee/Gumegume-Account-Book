package gumegumeCompany.gumegume_account_book.Fragment

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.navigation.findNavController
import com.kakao.sdk.user.UserApiClient
import gumegumeCompany.gumegume_account_book.LoginActivity
import gumegumeCompany.gumegume_account_book.MainActivity
import gumegumeCompany.gumegume_account_book.R
import gumegumeCompany.gumegume_account_book.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    //tag for logging
    private val TAG = "SettingsFragment"

    private var _binding: FragmentSettingsBinding?= null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        //현재 로그인한 사용자 정보 프로필에 표시
        var userName:String = "비회원"
        var userGender:String = "unKnown"
        var userBirthYear:String = "0000";
        var userBirthDate:String = "00/00";
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성공")
//                userName = user.kakaoAccount?.profile?.nickname.toString()
                binding.run {
                    profileUserName.text = "${user.kakaoAccount?.profile?.nickname}"

                    when(user.kakaoAccount?.gender.toString()){
                        "FEMALE" -> profileUserGender.setImageDrawable(resources.getDrawable(R.drawable.venus_18))
                        "MALE" -> profileUserGender.setImageDrawable(resources.getDrawable(R.drawable.mars_18))
                        else -> profileUserGender.setImageDrawable(resources.getDrawable(R.drawable.money_18))
                    }
                    profileUserBirth.text = "=${user?.kakaoAccount?.birthday}"
                }
                userGender = user.kakaoAccount?.gender.toString()
                userBirthYear = user.kakaoAccount?.birthyear.toString()
                userBirthDate = user.kakaoAccount?.legalBirthDate.toString()

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
//            UserApiClient.instance.logout { error ->
//                if (error != null) {
//                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
//                }
//                else {
//                    activity?.let{
//                        Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
//                        val intent = Intent(context, LoginActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        startActivity(intent)
//                    }
//                }
//            }

            //!!!!!!!!로그아웃 생략 - 테스트 용 코드
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        //문의하기
        binding.contactChatbotBtn.setOnClickListener {
            //TODO: implement chatbot
        }

        binding.contactEmailBtn.setOnClickListener {
            val developerEmails : Array<String> = arrayOf("sheisthepoem@naver.com", "kimsw215@naver.com")

            val contactEmailIntent = Intent(Intent.ACTION_SEND).apply{
                type = "*/*"
                putExtra(Intent.EXTRA_EMAIL, developerEmails)
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_title))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.contact_body))
            }
            if(contactEmailIntent.resolveActivity(requireActivity().packageManager) != null){
                Log.d(TAG, "start contact email intent")
                startActivity(contactEmailIntent)
            }else{
                Log.d(TAG, "could not start contact email intent")
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}