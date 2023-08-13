package gumegumeCompany.gumegume_account_book.AccountData

import gumegumeCompany.gumegume_account_book.R

data class AccountInfo (
    var date : String="0000/00/00",
    var type: String="수입",
    var categoryType: String= "월급",
    var title: String="제목 없음",
    var content : String? = null)