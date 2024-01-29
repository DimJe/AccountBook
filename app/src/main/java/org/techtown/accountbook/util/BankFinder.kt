package org.techtown.accountbook.util

import timber.log.Timber
import java.util.regex.Matcher
import java.util.regex.Pattern


object BankFinder {
    const val kakao = "com.kakaobank.channel"

    fun isAppending(text:String): Boolean{

        return (text.contains("출금")||text.contains("결제"))
    }
    fun checkMoney(text: String): String{

        val regex = "(\\d+(?:,\\d+)*)원"

        val pattern: Pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(text)
        var data = ""
        return if (matcher.find()) {
            data = matcher.group()
            data = data.replace(",","",)
            Timber.d("추출된 값: $data")
            data
        } else {
            Timber.d("일치하는 값 없음")
            "null"
        }

    }
}