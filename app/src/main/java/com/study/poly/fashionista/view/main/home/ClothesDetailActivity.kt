package com.study.poly.fashionista.view.main.home

import android.os.Bundle
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.study.poly.fashionista.base.BaseActivity
import com.study.poly.fashionista.data.entity.ClothesEntity
import com.study.poly.fashionista.databinding.ActivityClothesDetailBinding
import com.study.poly.fashionista.utility.hideUI
import com.study.poly.fashionista.utility.onThrottleFirstClick
import com.study.poly.fashionista.utility.toast
import com.study.poly.fashionista.utility.visibleUI
import com.study.poly.fashionista.view.adapter.BannerAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class ClothesDetailActivity :
    BaseActivity<ActivityClothesDetailBinding>({ ActivityClothesDetailBinding.inflate(it) }),
    CoroutineScope {

    companion object {
        const val CATEGORY_PATH = "category_path"
        const val IMAGE_PATH = "image_path"
    }

    private lateinit var db: CollectionReference
    private var clothesInfo = ClothesEntity()

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewInit()
    }

    private fun viewInit() = with(binding) {

        layoutTitle.titleTv.text = "상세정보"
        layoutTitle.btnBack.setOnClickListener { onBackPressed() }

        notNetworkLayout.refreshBtn.onThrottleFirstClick {
            notNetworkLayout.root.hideUI()
            getData()
        }

        getData()
    }

    private fun getData() {

        launch {
            try {
                showProgress()
                getDetailInfo()
            } catch (e: UnknownHostException) {
                binding.notNetworkLayout.root.visibleUI()
            } catch (e: Exception) {
                toast("error:$e")
            }
        }
    }

    private suspend fun showProgress() = withContext(coroutineContext) {
        binding.loadingBar.root.visibleUI()
    }

    private suspend fun dismissProgress() = withContext(coroutineContext) {
        binding.loadingBar.root.hideUI()
    }

    private suspend fun getDetailInfo() = withContext(Dispatchers.IO) {

        val categoryPath = intent.getStringExtra(CATEGORY_PATH).toString()
        val imagePath = intent.getStringExtra(IMAGE_PATH).toString()

        db = FirebaseFirestore.getInstance().collection(categoryPath)
        db.whereEqualTo("TitlePath", imagePath).get().await().documents.forEach { document ->
            document.toObject(ClothesEntity::class.java)?.let { data ->
                clothesInfo = data
            }
        }.run {
            withContext(Dispatchers.Main) {
                setViewPager()
                setClothesInfo()
                dismissProgress()
            }
        }
    }

    private fun setViewPager() = with(binding) {

        val imageUrl = arrayListOf(
            clothesInfo.TitlePath,
            clothesInfo.ImageUrl?.get(0).toString(),
            clothesInfo.ImageUrl?.get(1).toString()
        )

        clothesViewpager.adapter = BannerAdapter(imageUrl)
        dotIndicator.setViewPager2(clothesViewpager)
    }

    private fun setClothesInfo() = with(binding) {

        clothesInfo.let { info ->
            clothesShopTv.text = "쇼핑몰: ${info.Shop}"
            clothesNameTv.text = "옷이름: ${info.Name}"
            clothesInfoTv.text = info.Info
        }
    }

    override fun onDestroy() {
        coroutineContext.cancel()
        super.onDestroy()
    }

}