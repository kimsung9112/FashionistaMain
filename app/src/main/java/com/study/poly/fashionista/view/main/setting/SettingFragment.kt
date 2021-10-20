package com.study.poly.fashionista.view.main.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.study.poly.fashionista.base.BaseFragment
import com.study.poly.fashionista.databinding.FragmentSettingBinding
import com.study.poly.fashionista.utility.hideUI
import com.study.poly.fashionista.utility.moveNextAnim
import com.study.poly.fashionista.utility.onThrottleFirstClick

class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()
    }

    private fun initView() {
        binding.titleLayout.titleTv.text = "내 프로필"
        binding.titleLayout.btnBack.hideUI()
    }

    private fun initListener() {

        binding.myClothesTv.onThrottleFirstClick {
            val intent = Intent(requireContext(), MyClothesActivity::class.java)
            startActivity(intent)
            requireActivity().moveNextAnim()
        }

        binding.secessionTv.onThrottleFirstClick {

        }

        binding.loginOutTv.onThrottleFirstClick {

        }

    }

}