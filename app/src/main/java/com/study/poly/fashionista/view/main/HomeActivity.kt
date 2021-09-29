package com.study.poly.fashionista.view.main

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.study.poly.fashionista.R
import com.study.poly.fashionista.base.BaseActivity
import com.study.poly.fashionista.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity<ActivityHomeBinding>({ ActivityHomeBinding.inflate(it) }) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewInit()
    }

    private fun viewInit() {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_host) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomMenu, navController)
    }


}

