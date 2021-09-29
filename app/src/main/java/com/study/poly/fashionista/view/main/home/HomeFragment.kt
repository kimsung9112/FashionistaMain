package com.study.poly.fashionista.view.main.home


import android.os.Bundle
import android.view.View
import com.google.firebase.storage.FirebaseStorage
import com.study.poly.fashionista.base.BaseFragment
import com.study.poly.fashionista.databinding.FragmentHomeBinding
import com.study.poly.fashionista.utility.hideUI
import com.study.poly.fashionista.utility.onThrottleFirstClick
import com.study.poly.fashionista.utility.toast
import com.study.poly.fashionista.utility.visibleUI
import com.study.poly.fashionista.view.adapter.BannerAdapter
import com.study.poly.fashionista.view.adapter.MainClothesAdapter
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.tasks.await


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    CoroutineScope {


    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val storageRef = FirebaseStorage.getInstance()
    private val arrayPath = arrayListOf(
        "/main_img/banner",
        "/main_img/hood",
        "/main_img/outer",
        "/main_img/pants",
        "/main_img/t_shirt"
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

    private fun viewInit() {


        binding.notNetworkLayout.refreshBtn.onThrottleFirstClick {
            binding.notNetworkLayout.root.hideUI()
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

        bannerList.clear()
        hoodList.clear()
        outerList.clear()
        pantsList.clear()
        tShirtList.clear()

        arrayPath.forEach { path ->
            val listClothes = storageRef.reference.child(path).listAll()

            when (path) {
                "/main_img/banner" -> {
                    listClothes.await().items.forEach { storage ->
                        bannerList.add(storage.downloadUrl.await().toString())
                    }
                }
                "/main_img/hood" -> {
                    listClothes.await().items.forEach { storage ->
                        hoodList.add(storage.downloadUrl.await().toString())
                    }
                }
                "/main_img/outer" -> {
                    listClothes.await().items.forEach { storage ->
                        outerList.add(storage.downloadUrl.await().toString())
                    }
                }
                "/main_img/pants" -> {
                    listClothes.await().items.forEach { storage ->
                        pantsList.add(storage.downloadUrl.await().toString())
                    }
                }
                "/main_img/t_shirt" -> {
                    listClothes.await().items.forEach { storage ->
                        tShirtList.add(storage.downloadUrl.await().toString())
                    }
                }
            }
        }.run {

            withContext(Dispatchers.Main) {
                setViewPager()
                setRecyclerView()
                dismissProgress()
            }
        }

    }

    private fun setViewPager() = with(binding) {

        slideViewpager.adapter = BannerAdapter(bannerList)
        dotIndicator.setViewPager2(slideViewpager)
    }

    private fun setRecyclerView() = with(binding) {

        layoutHood.clothesName.text = "HOOD"
        layoutOvercoat.clothesName.text = "OUTER"
        layoutPants.clothesName.text = "PANTS"
        layoutShirt.clothesName.text = "T-SHIRT"

        layoutHood.clothesRecyclerview.let { list ->
            list.adapter = MainClothesAdapter(hoodList)
        }
        layoutOvercoat.clothesRecyclerview.let { list ->
            list.adapter = MainClothesAdapter(outerList)
        }
        layoutPants.clothesRecyclerview.let { list ->
            list.adapter = MainClothesAdapter(pantsList)
        }
        layoutShirt.clothesRecyclerview.let { list ->
            list.adapter = MainClothesAdapter(tShirtList)
        }
    }

    override fun onDestroy() {
        coroutineContext.cancel()
        super.onDestroy()
    }

}
