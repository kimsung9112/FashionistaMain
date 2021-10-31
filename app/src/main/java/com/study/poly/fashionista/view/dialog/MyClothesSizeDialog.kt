package com.study.poly.fashionista.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.study.poly.fashionista.databinding.DialogMyClothesSizeBinding
import com.study.poly.fashionista.utility.onThrottleFirstClick
import com.study.poly.fashionista.utility.toast

class MyClothesSizeDialog(context: Context,private val bmiCallback : IBmiResult) :
    Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen) {

    private lateinit var binding: DialogMyClothesSizeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogMyClothesSizeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        viewInit()
    }

    private fun viewInit() = with(binding) {

        val clothesList = ArrayList<String>()

//        hoodTv.setOnClickListener {
//
//            if (!hoodTv.isSelected) {
//                hoodTv.isSelected = true
//                clothesList.add(hoodTv.text.toString())
//            } else {
//                hoodTv.isSelected = false
//                clothesList.remove(hoodTv.text.toString())
//            }
//        }
//        pantsTv.setOnClickListener {
//
//            if (!pantsTv.isSelected) {
//                pantsTv.isSelected = true
//                clothesList.add(pantsTv.text.toString())
//            } else {
//                pantsTv.isSelected = false
//                clothesList.remove(pantsTv.text.toString())
//            }
//        }
//        tShirtTv.setOnClickListener {
//
//            if (!tShirtTv.isSelected) {
//                tShirtTv.isSelected = true
//                clothesList.add(tShirtTv.text.toString())
//            } else {
//                tShirtTv.isSelected = false
//                clothesList.remove(tShirtTv.text.toString())
//            }
//        }
//        outerTv.setOnClickListener {
//
//            if (!outerTv.isSelected) {
//                outerTv.isSelected = true
//                clothesList.add(outerTv.text.toString())
//            } else {
//                outerTv.isSelected = false
//                clothesList.remove(outerTv.text.toString())
//            }
//        }

        btnCancel.onThrottleFirstClick {
            dismiss()
        }

        btnCheck.onThrottleFirstClick {
            checkInfo(clothesList)
        }
    }

    private fun checkInfo(clothesTypeList: ArrayList<String>) = with(binding) {

        when {
            heightEditTv.text.isEmpty() -> {
                context.toast("키를 작성해주세요.")
            }
            weightEditTv.text.isEmpty() -> {
                context.toast("몸무게를 작성해주세요.")
            }
            else -> {
                calculateBmi()
            }
        }
    }

    private fun calculateBmi() = with(binding){
        var height = heightEditTv.text.toString()
        var weight = weightEditTv.text.toString()
        var bmi = weight.toInt() / ((height.toDouble()/100)*(height.toDouble()/100))

        var size : String = when{
            bmi > 30 -> "XXL"
            bmi > 25 -> "XL"
            bmi > 23 -> "L"
            bmi > 18 -> "M"
            else -> "S"
        }
        bmiCallback.getSize(size)

        context.toast("내사이즈는 '$size'입니다")

        dismiss()
    }

}
