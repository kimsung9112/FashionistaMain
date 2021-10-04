package com.study.poly.fashionista.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.study.poly.fashionista.R
import com.study.poly.fashionista.databinding.DialogMyClothesSizeBinding

// 레이아웃 만들어야함
class MyClothesSizeDialog(context: Context) :
    Dialog(context) {

    private lateinit var binding: DialogMyClothesSizeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogMyClothesSizeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewInit()
    }

    private fun viewInit() {

        editLayout()
    }

    private fun editLayout() = with(binding) {


    }

}
