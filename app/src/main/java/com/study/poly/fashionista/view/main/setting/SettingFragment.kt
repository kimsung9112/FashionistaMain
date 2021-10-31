package com.study.poly.fashionista.view.main.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.study.poly.fashionista.base.BaseFragment
import com.study.poly.fashionista.databinding.FragmentSettingBinding
import com.study.poly.fashionista.utility.hideUI
import com.study.poly.fashionista.utility.moveNextAnim
import com.study.poly.fashionista.utility.onThrottleFirstClick
import com.study.poly.fashionista.view.login.IntroActivity

class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {

    private var email = FirebaseAuth.getInstance().currentUser?.email.toString()
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()
    }

    private fun initView() {
        binding.titleLayout.titleTv.text = "내 프로필"
        binding.titleLayout.btnBack.hideUI()
        binding.profileNickname.text = email
    }

    private fun initListener() {

        binding.myClothesTv.onThrottleFirstClick {
            val intent = Intent(requireContext(), MyClothesActivity::class.java)
            startActivity(intent)
            requireActivity().moveNextAnim()
        }

        binding.secessionTv.setOnClickListener() {
            firebaseAuth.currentUser?.delete()

            Toast.makeText(getActivity(),"회원탈퇴!",Toast.LENGTH_SHORT).show();

            val intent = Intent(getActivity(), IntroActivity::class.java)
            startActivity(intent)
        }

        binding.loginOutTv.onThrottleFirstClick {
            firebaseAuth.signOut()
            Toast.makeText(getActivity(),"로그아웃!",Toast.LENGTH_SHORT).show();

            val intent = Intent(getActivity(), IntroActivity::class.java)
            startActivity(intent)

        }

    }

}