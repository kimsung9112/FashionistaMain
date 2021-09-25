package com.study.poly.fashionista.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.*
import com.google.android.gms.auth.api.Auth.GoogleSignInApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.study.poly.fashionista.R
import com.study.poly.fashionista.base.BaseActivity
import com.study.poly.fashionista.databinding.ActivityLoginBinding
import com.study.poly.fashionista.utility.moveNextAnim
import com.study.poly.fashionista.utility.toast
import com.study.poly.fashionista.view.main.HomeActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>({ ActivityLoginBinding.inflate(it) }) {


    private var auth: FirebaseAuth? = null

    private var googleSignInClient: GoogleSignInClient? = null

    private var resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(StartActivityForResult()) { result ->

            if (result?.data != null) {
                result.data?.let { googleLoginSuccess(it) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        viewInit()
    }

    private fun viewInit() {

        auth = Firebase.auth

        val googleOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleOption)

        binding.btnGoogleLogin.setOnClickListener {
            googleLogin()
        }

    }

    private fun googleLogin() {

        val sigiInInIntent = googleSignInClient?.signInIntent
        resultLauncher.launch(sigiInInIntent)
    }

    private fun googleLoginSuccess(data: Intent) {

        val result = GoogleSignInApi.getSignInResultFromIntent(data)

        try {

            if (result.isSuccess) {
                val account = result.signInAccount
                firebaseAuthWithGoogle(account)
            } else {
                toast("error ${result.status}")
            }

        } catch (e: ApiException) {
            Log.d("로그", "error:$e")
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    moveMainPage(auth?.currentUser)
                }
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