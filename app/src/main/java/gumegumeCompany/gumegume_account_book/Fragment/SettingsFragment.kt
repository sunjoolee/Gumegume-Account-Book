package gumegumeCompany.gumegume_account_book.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kakao.sdk.user.UserApiClient
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

        //TODO: 사용자 정보 받아와 프로필에 표시하기
        //사용자 이름
        //사용자 성별
        //사용자 생년월일

        binding.logoutBtn.setOnClickListener {

        }

        //문의하기
        binding.contactChatbotBtn.setOnClickListener {

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