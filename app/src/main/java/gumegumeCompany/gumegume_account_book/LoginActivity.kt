package gumegumeCompany.gumegume_account_book

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import gumegumeCompany.gumegume_account_book.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var activityLoginBinding: ActivityLoginBinding

    // 카카오계정으로 로그인 공통 callback 구성
    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("카카오로그인", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i("카카오로그인", "카카오계정으로 로그인 성공 ${token.accessToken}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        KakaoSdk.init(this, getString(R.string.kakao_native_key))

        /* HashKey확인
        val keyHash = Utility.getKeyHash(this)
        Log.e("Key", "keyHash: ${keyHash}")*/

        activityLoginBinding.kakaoLoginBtn.setOnClickListener {
            kakao_login(this@LoginActivity)
        }
    }

    // 토큰 정보 확인

    // 토큰 정보 보기
    fun kakako_token() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.e("카카오토큰", "토큰 정보 보기 실패", error)
                } else if (tokenInfo != null) {

                    // 토큰이 있을 때 사용자 정보 미리 가져와서 운세 보여주기
                    Log.i(
                        "카카오토큰", "토큰 정보 보기 성공" +
                                "\n회원번호: ${tokenInfo.id}" +
                                "\n만료시간: ${tokenInfo.expiresIn} 초"
                    )
                }
            }
        }
    }


    // kakao login
    fun kakao_login(context: Context) {
        // 토큰이 있으면 kakao login 정보에서 운세 받아올 수 있나 ?
        // 카카오 로그인 정보 확인
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("카카오로그인", "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i("카카오로그인", "카카오톡으로 로그인 성공 ${token.accessToken}")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}


/*
// 로그아웃
UserApiClient.instance.logout
{
    error ->
    if (error != null) {
        Log.d("카카오", "카카오 로그아웃 실패")
    } else {
        Log.d("카카오", "카카오 로그아웃 성공!")
    }
}

// 연결 끊기
UserApiClient.instance.unlink { error ->
    if (error != null) {
        Log.e(TAG, "연결 끊기 실패", error)
    }
    else {
        Log.i(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
    }
}

*/


