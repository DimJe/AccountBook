package org.techtown.accountbook.UI.dialog

import java.util.*

interface DialogListener {
    fun submitData(money: Int, type: String,date: Date)
}