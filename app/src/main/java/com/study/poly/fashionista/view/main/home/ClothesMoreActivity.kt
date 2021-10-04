package com.study.poly.fashionista.view.main.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.study.poly.fashionista.R
import com.study.poly.fashionista.base.BaseActivity
import com.study.poly.fashionista.databinding.ActivityClothesMoreBinding

class ClothesMoreActivity :
    BaseActivity<ActivityClothesMoreBinding>({ ActivityClothesMoreBinding.inflate(it) }) {

    companion object {
        const val CLOTHES_TYPE = "clothes_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewInit()
    }

    private fun viewInit() {

    }



}