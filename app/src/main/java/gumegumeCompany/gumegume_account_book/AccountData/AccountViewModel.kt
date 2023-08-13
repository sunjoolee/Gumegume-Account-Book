package gumegumeCompany.gumegume_account_book.AccountData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AccountViewModel : ViewModel() {
    val accountInfo: MutableLiveData<AccountInfo> by lazy {
        MutableLiveData<AccountInfo>()
    }
}