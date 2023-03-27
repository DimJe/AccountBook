package org.techtown.accountbook.UI.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import org.techtown.accountbook.UI.custom.CustomRadioBtn
import org.techtown.accountbook.databinding.DialogAddSpendingBinding

class AddSpendingDataDialog(dialogListener: DialogListener) : DialogFragment() {

    private var _binding: DialogAddSpendingBinding? = null
    private val binding get() = _binding!!
    private var clicked: CustomRadioBtn? = null

    private var dialogListener: DialogListener? = null

    init {
        this.dialogListener = dialogListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("태그", "onCreateView:called ")
        _binding = DialogAddSpendingBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.gridLayout.children.forEach { view ->
            view.setOnClickListener {
                when (clicked) {
                    null -> {
                        clicked = it as CustomRadioBtn
                        it.isSelected = !it.isSelected
                    }
                    it as CustomRadioBtn -> {
                        Log.d("태그", "onCreateView:?? ")
                        clicked!!.isSelected = !(clicked!!.isSelected)
                        clicked = null
                    }
                    else -> {
                        clicked!!.isSelected = !(clicked!!.isSelected)
                        clicked = it
                        clicked!!.isSelected = !(clicked!!.isSelected)
                    }
                }
            }
        }
        binding.submit.setOnClickListener {
            if(binding.editMoney.text.toString().isNotBlank() && clicked != null){
                this.dialogListener!!.submitData(binding.editMoney.text.toString().toInt(),clicked!!.getType())
            }
            else Toast.makeText(requireContext(), "소비 정보를 다시 한번 확인해주세요.", Toast.LENGTH_SHORT).show()
        }
        binding.cancel.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}