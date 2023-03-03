package org.techtown.accountbook.UI.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import org.techtown.accountbook.R
import org.techtown.accountbook.databinding.CustomBtnBinding

class CustomBtn(context: Context,attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private lateinit var textView: TextView
    private lateinit var imgView: ImageView
    private var img: Int = 0
    private lateinit var text: String
    init {
        initAttrs(attrs)
        init(context)
    }
    private fun init(context: Context?){
        inflate(context,R.layout.custom_btn,this)
        textView = findViewById(R.id.text)
        imgView = findViewById(R.id.img)

        textView.text = text
        Glide.with(this)
            .load(img)
            .into(imgView)
    }
    private fun initAttrs(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomBtn,
            0, 0
        ).apply {
            // 속성으로 전달받은 값을 대입하는 부분
            try {
                img = getResourceId(R.styleable.CustomBtn_img, R.drawable.ic_launcher_foreground)
                text = getString(R.styleable.CustomBtn_title) ?: ""
            } finally {
                recycle()
            }
        }
    }
    fun hideText(){

        layoutParams.width = LayoutParams.WRAP_CONTENT
        textView.animate().scaleX(1f).setDuration(150).withEndAction {
            textView.visibility = View.GONE
        }
    }
    fun showText(){

        layoutParams.width = 0
        textView.animate().scaleX(1f).setDuration(150).withEndAction {
            textView.visibility = View.VISIBLE
        }
    }
}