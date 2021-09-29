package com.study.poly.fashionista.utility

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.study.poly.fashionista.R

fun Activity.moveNextAnim() {
    overridePendingTransition(R.anim.page_right_in, R.anim.page_left_out)
}

fun Activity.movePrevAnim() {
    overridePendingTransition(R.anim.page_left_in, R.anim.page_right_out)
}

fun Activity.moveFade() {
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun View.hideUI() {
    visibility = View.GONE
}

fun View.visibleUI() {
    visibility = View.VISIBLE
}

fun ImageView.loadImage(imgUrl: String) {
    Glide.with(context)
        .load(imgUrl)
        .placeholder(R.color.theme_sub_color)
        .error(R.color.theme_sub_color)
        .into(this)
}

fun ImageView.loadCircleImage(imgUrl: String) {

    Glide.with(context)
        .load(imgUrl)
        .circleCrop()
        .apply(RequestOptions.circleCropTransform())
        .placeholder(R.drawable.round_circle_gray)
        .error(R.drawable.round_circle_gray)
        .into(this)
}

fun EditText.onMyTextChange(completion: (Editable) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        override fun afterTextChanged(editable: Editable) {
            completion(editable)
        }
    })
}

var isClicked = false

fun View.onThrottleFirstClick(interval: Long = 1000L, action: (v: View) -> Unit) {
    setOnClickListener { v ->
        if (isClicked.not()) {
            isClicked = true
            v?.run {
                postDelayed({
                    isClicked = false
                }, interval)
                action(v)
            }
        }
    }
}