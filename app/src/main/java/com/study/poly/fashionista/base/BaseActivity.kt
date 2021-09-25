package com.study.poly.fashionista.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.study.poly.fashionista.R
import com.study.poly.fashionista.utility.movePrevAnim

abstract class BaseActivity<B : ViewBinding>(private val bindingFactory: (LayoutInflater) -> B) :
    AppCompatActivity() {

    private var _binding: B? = null
    val binding: B get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        movePrevAnim()
    }

    fun setStatusBar(colors: Int = R.color.black) {

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(colors, null)

        } else {
            window.statusBarColor = ContextCompat.getColor(this, colors)
        }
    }

    fun notFoundSaveData() {

    }
}