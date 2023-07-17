package gumegumeCompany.gumegume_account_book.AccountInfo

import java.time.LocalDate

data class AccountInfo (
    val date: String,
    val type: String,
    val categoryType: String,
    val title: String,
    val memo: String
    )