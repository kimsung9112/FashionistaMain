package com.study.poly.fashionista.view.main.home

import android.content.Intent
import android.os.Bundle
import android.view.View

import com.google.firebase.storage.FirebaseStorage

import com.study.poly.fashionista.R
import com.study.poly.fashionista.base.BaseFragment
import com.study.poly.fashionista.databinding.FragmentHomeBinding
import com.study.poly.fashionista.utility.*
import com.study.poly.fashionista.utility.Constant.CATEGORY_HOOD
import com.study.poly.fashionista.utility.Constant.CATEGORY_OUTER
import com.study.poly.fashionista.utility.Constant.CATEGORY_PANTS
import com.study.poly.fashionista.utility.Constant.CATEGORY_T_SHIRT

import com.study.poly.fashionista.utility.Constant.PATH_BANNER
import com.study.poly.fashionista.utility.Constant.PATH_HOOD
import com.study.poly.fashionista.utility.Constant.PATH_OUTER
import com.study.poly.fashionista.utility.Constant.PATH_PANTS
import com.study.poly.fashionista.utility.Constant.PATH_T_SHIRT

import com.study.poly.fashionista.view.adapter.BannerAdapter
import com.study.poly.fashionista.view.adapter.MainClothesAdapter

import java.lang.Exception
import java.net.UnknownHostException

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    CoroutineScope {

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val storageRef = FirebaseStorage.getInstance()

    private val arrayPath = arrayListOf(
        PATH_BANNER,
        PATH_HOOD,
        PATH_OUTER,
        PATH_PANTS,
        PATH_T_SHIRT
    )

    /** 배너 */
    private val bannerList = ArrayList<String>()

    /** 옷  */
    private val hoodList = ArrayList<String>()
    private val outerList = ArrayList<String>()
    private val pantsList = ArrayList<String>()
    private val tShirtList = ArrayList<String>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewInit()
    }

    private fun viewInit() = with(binding) {

        layoutShirt.clothesName.text = getString(R.string.main_clothes_t_shirt)
        layoutHood.clothesName.text = getString(R.string.main_clothes_hood)
        layoutOvercoat.clothesName.text = getString(R.string.main_clothes_outer)
        layoutPants.clothesName.text = getString(R.string.main_clothes_pants)

        notNetworkLayout.refreshBtn.onThrottleFirstClick {
            notNetworkLayout.root.hideUI()
            getData()
        }

        getData()
    }

    private fun getData() = with(binding) {
        launch {
            try {
                getImageUrl()
            } catch (e: UnknownHostException) {
                notNetworkLayout.root.visibleUI()
            } catch (e: Exception) {
                context?.toast(e.message.toString())
            }
        }
    }

    private suspend fun showProgress() = withContext(coroutineContext) {
        binding.loadingBar.root.visibleUI()
    }

    private suspend fun dismissProgress() = withContext(coroutineContext) {
        binding.loadingBar.root.hideUI()
    }

    private suspend fun getImageUrl() = withContext(Dispatchers.IO) {

        showProgress()

        when (bannerList.size) {

            0 -> {

                arrayPath.forEach { path ->

                    val listClothes = storageRef.reference.child(path).listAll()

                    when (path) {
                        PATH_BANNER -> {
                            listClothes.await().items.forEach { storage ->
                                bannerList.add(storage.downloadUrl.await().toString())
                            }
                        }
                        PATH_HOOD -> {
                            listClothes.await().items.forEach { storage ->
                                hoodList.add(storage.downloadUrl.await().toString())
                            }

                        }
                        PATH_OUTER -> {
                            listClothes.await().items.forEach { storage ->
                                outerList.add(storage.downloadUrl.await().toString())
                            }

                        }
                        PATH_PANTS -> {
                            listClothes.await().items.forEach { storage ->
                                pantsList.add(storage.downloadUrl.await().toString())
                            }

                        }
                        PATH_T_SHIRT -> {
                            listClothes.await().items.forEach { storage ->
                                tShirtList.add(storage.downloadUrl.await().toString())
                            }

                        }
                    }
                }
            }

            else -> {
                return@withContext
            }
        }

        withContext(Dispatchers.Main) {
            setViewPager()
            setRecyclerView()
            dismissProgress()
        }

    }

    private fun setViewPager() = with(binding) {

        slideViewpager.adapter = BannerAdapter(bannerList)
        dotIndicator.setViewPager2(slideViewpager)
    }

    private fun setRecyclerView() = with(binding) {

        val intent = Intent(requireContext(), ClothesDetailActivity::class.java)

        layoutHood.clothesRecyclerview.let { list ->
            list.adapter = MainClothesAdapter(hoodList) { url ->
                intent.putExtra(ClothesDetailActivity.CATEGORY_PATH, CATEGORY_HOOD)
                intent.putExtra(ClothesDetailActivity.IMAGE_PATH, url)
                moveNext(intent)
            }
        }
        layoutOvercoat.clothesRecyclerview.let { list ->
            list.adapter = MainClothesAdapter(outerList) { url ->
                intent.putExtra(ClothesDetailActivity.CATEGORY_PATH, CATEGORY_OUTER)
                intent.putExtra(ClothesDetailActivity.IMAGE_PATH, url)
                moveNext(intent)
            }
        }
        layoutPants.clothesRecyclerview.let { list ->
            list.adapter = MainClothesAdapter(pantsList) { url ->
                intent.putExtra(ClothesDetailActivity.CATEGORY_PATH, CATEGORY_PANTS)
                intent.putExtra(ClothesDetailActivity.IMAGE_PATH, url)
                moveNext(intent)
            }
        }
        layoutShirt.clothesRecyclerview.let { list ->
            list.adapter = MainClothesAdapter(tShirtList) { url ->
                intent.putExtra(ClothesDetailActivity.CATEGORY_PATH, CATEGORY_T_SHIRT)
                intent.putExtra(ClothesDetailActivity.IMAGE_PATH, url)
                moveNext(intent)
            }
        }

        btnMoreClickListener()
    }

    private fun btnMoreClickListener() = with(binding) {

        val intent = Intent(requireContext(), ClothesMoreActivity::class.java)

        layoutHood.btnMoreClothes.onThrottleFirstClick {
            intent.putExtra(ClothesMoreActivity.CLOTHES_TYPE, ClothesType.HOOD.clothes)
            moveNext(intent)
        }
        layoutOvercoat.btnMoreClothes.onThrottleFirstClick {
            intent.putExtra(ClothesMoreActivity.CLOTHES_TYPE, ClothesType.OUTER.clothes)
            moveNext(intent)

        }
        layoutPants.btnMoreClothes.onThrottleFirstClick {
            intent.putExtra(ClothesMoreActivity.CLOTHES_TYPE, ClothesType.PANTS.clothes)
            moveNext(intent)

        }
        layoutShirt.btnMoreClothes.onThrottleFirstClick {
            intent.putExtra(ClothesMoreActivity.CLOTHES_TYPE, ClothesType.T_SHIRT.clothes)
            moveNext(intent)
        }
    }

    private fun moveNext(intent: Intent) {
        startActivity(intent)
        requireActivity().moveNextAnim()
    }

    override fun onDestroy() {
        coroutineContext.cancel()
        super.onDestroy()
    }

}
