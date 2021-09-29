package com.study.poly.fashionista.view.login

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.study.poly.fashionista.R
import com.study.poly.fashionista.base.BaseActivity
import com.study.poly.fashionista.databinding.ActivityIntroBinding
import com.study.poly.fashionista.utility.moveNextAnim
import com.study.poly.fashionista.utility.toast
import android.content.pm.PackageManager

import android.content.pm.PackageInfo
import android.util.Base64
import com.google.firebase.auth.FirebaseAuth
import com.study.poly.fashionista.view.main.HomeActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class IntroActivity : BaseActivity<ActivityIntroBinding>({ ActivityIntroBinding.inflate(it) }) {

    companion object {
        // 모바일 네트워크 상태
        private const val NO_CONNECTION = "No_Connection"
        private const val WIFI_STATE = "Wifi_State"
        private const val CELLULAR_STATE = "Cellular_State"
    }

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val security = checkSecurity()
        val networkState = getNetworkType()
        if (security) {

            when (networkState) {
                NO_CONNECTION -> toast(getString(R.string.server_network_exception))
                else -> viewInit()
            }
        } else {
            finish()
            return
        }
    }

    private fun checkSecurity(): Boolean {

        return try {
            Runtime.getRuntime().exec("su")
            toast("보안 문제가 있는 폰입니다. 서비스를 종료하세요.")
            false
        } catch (e: Exception) {
            true
        }
    }

    private fun getNetworkType(): String {
        val connMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connMgr.activeNetwork ?: return NO_CONNECTION
        val actNw = connMgr.getNetworkCapabilities(nw) ?: return NO_CONNECTION

        return when {

            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> WIFI_STATE
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> CELLULAR_STATE
            else -> NO_CONNECTION
        }
    }

    private fun viewInit() {

        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = if (auth.currentUser == null) {
                    Intent(this, LoginActivity::class.java)
                } else {
                    Intent(this, HomeActivity::class.java)
                }
                startActivity(intent)
                moveNextAnim()
            }, 1000
        )
    }


}