package com.study.poly.fashionista.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.study.poly.fashionista.R
import com.study.poly.fashionista.base.BaseActivity
import com.study.poly.fashionista.databinding.ActivityLoginBinding
import com.study.poly.fashionista.utility.*
import com.study.poly.fashionista.view.main.HomeActivity
import java.util.*

class LoginActivity : BaseActivity<ActivityLoginBinding>({ ActivityLoginBinding.inflate(it) }) {


    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        viewInit()
    }

    private fun viewInit() {

        binding.joinBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
            moveNextAnim()
        }

        binding.loginBtn.setOnClickListener {
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
                firebaseSendCheck()
                infoStateTv.hideUI()
            }
        }
    }

    private fun firebaseSendCheck() = with(binding) {

        val email = binding.emailEdit.text.toString()
        val password = binding.passwordEdit.text.toString()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@LoginActivity) { task ->

                if (task.isSuccessful) {

                    moveMainPage(firebaseAuth.currentUser)
                }

            }
            .addOnFailureListener {
                when {

                    Constant.AuthOverLap in it.toString() -> {
                        infoStateTv.text = "이미 존재하는 계정입니다."
                    }

                    Constant.EmailFormError in it.toString() -> {
                        infoStateTv.text = "이메일 형식이 아닙니다."
                    }

                    else -> {
                        infoStateTv.text = it.toString()
                    }
                }

                infoStateTv.visibleUI()
            }
    }

    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            moveNextAnim()
            finish()
        }
    }

}