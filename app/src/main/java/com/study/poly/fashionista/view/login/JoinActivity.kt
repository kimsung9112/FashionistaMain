package com.study.poly.fashionista.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.study.poly.fashionista.R
import com.study.poly.fashionista.base.BaseActivity
import com.study.poly.fashionista.databinding.ActivityJoinBinding
import com.study.poly.fashionista.utility.Constant.AuthOverLap
import com.study.poly.fashionista.utility.Constant.EmailFormError
import com.study.poly.fashionista.utility.hideUI
import com.study.poly.fashionista.utility.visibleUI

class JoinActivity : BaseActivity<ActivityJoinBinding>({ ActivityJoinBinding.inflate(it) }) {


    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewInit()
    }

    private fun viewInit() = with(binding) {

        titleLayout.titleTv.text = "회원가입"
        titleLayout.btnBack.setOnClickListener { onBackPressed() }

        joinBtn.setOnClickListener {
            authCheck()
        }
    }

    private fun authCheck() = with(binding) {

        when {
            emailEdit.text.isEmpty() -> {
                infoStateTv.text = "아이디를 입력해주세요."
                infoStateTv.visibleUI()
            }

            passwordEdit.text.isEmpty() -> {
                infoStateTv.text = "패스워드를 입력해주세요."
                infoStateTv.visibleUI()
            }

            else -> {
                firebaseAuthSend()
                infoStateTv.hideUI()
            }
        }
    }

    private fun firebaseAuthSend() = with(binding) {

        val email = binding.emailEdit.text.toString()
        val password = binding.passwordEdit.text.toString()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    onBackPressed()
                }
            }
            .addOnFailureListener {

                when  {

                    AuthOverLap in it.toString() -> {
                        infoStateTv.text = "이미 존재하는 계정입니다."
                    }

                    EmailFormError in it.toString() -> {
                        infoStateTv.text = "이메일 형식이 아닙니다."
                    }

                    else -> {
                        infoStateTv.text = it.toString()
                    }
                }

                infoStateTv.visibleUI()
            }
    }
}