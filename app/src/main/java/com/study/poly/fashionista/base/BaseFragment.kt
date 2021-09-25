package com.study.poly.fashionista.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.study.poly.fashionista.R

typealias Inflate<B> = (LayoutInflater, ViewGroup?, Boolean) -> B

abstract class BaseFragment<B : ViewBinding>(private val inflate: Inflate<B>) : Fragment() {

    private var _binding: B? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
            : View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    fun setStatusBar(colors: Int = R.color.black) {

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.statusBarColor = resources.getColor(colors, null)

        } else {
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), colors)
        }
    }

    fun onBackPressed() {
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}