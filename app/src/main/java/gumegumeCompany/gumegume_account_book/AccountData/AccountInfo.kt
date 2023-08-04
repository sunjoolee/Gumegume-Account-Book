package gumegumeCompany.gumegume_account_book.AccountData

data class AccountInfo (
    var date : String,
    var type: String,
    var categoryType: String,
    var title: String,
    var content : String? = null)