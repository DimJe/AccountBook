package org.techtown.accountbook.UI.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import org.techtown.accountbook.R

class CustomRadioBtn(context: Context,attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private lateinit var textView: TextView
    private lateinit var imgView: CircleImageView
    private var img: Int = 0
    private var imgColor: Int = 0
    private lateinit var text: String

    init {
        initAttrs(attrs)
        init(context)
    }

    private fun init(context: Context?){
        inflate(context, R.layout.custom_radio_btn,this)
        textView = findViewById(R.id.radioText)
        imgView = findViewById(R.id.radioImg)

        textView.text = text
        Glide.with(this)
            .load(img)
            .into(imgView)
        imgView.setCircleBackgroundColorResource(imgColor)

    }
    private fun initAttrs(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomRadioBtn,
            0, 0
        ).apply {
            // 속성으로 전달받은 값을 대입하는 부분
            try {
                img = getResourceId(R.styleable.CustomRadioBtn_img, R.drawable.ic_launcher_foreground)
                text = getString(R.styleable.CustomRadioBtn_title) ?: ""
                imgColor = getResourceId(R.styleable.CustomRadioBtn_imgColor,R.color.white)
            } finally {
                recycle()
            }
        }
    }
    fun getType() = textView.text.toString()
}