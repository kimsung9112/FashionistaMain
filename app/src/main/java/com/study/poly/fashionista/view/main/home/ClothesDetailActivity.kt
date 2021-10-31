package com.study.poly.fashionista.view.main.home

import android.os.Bundle
import android.util.Size
import androidx.core.view.isGone
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.study.poly.fashionista.base.BaseActivity
import com.study.poly.fashionista.data.ClothesModel
import com.study.poly.fashionista.data.ClothesSaveModel
import com.study.poly.fashionista.databinding.ActivityClothesDetailBinding
import com.study.poly.fashionista.utility.Constant.USER_PATH
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

    private lateinit var categoryPath: String
    private lateinit var imagePath: String
    private lateinit var email: String

    private lateinit var saveDB: FirebaseFirestore
    private lateinit var db: CollectionReference
    private var clothesInfo = ClothesModel()

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
        btnSaveClothesInfo.onThrottleFirstClick {
            setClothesInfoSave()
        }

        getData()
    }


    private fun setClothesInfoSave() {
        val model = ClothesSaveModel(
            email,
            categoryPath,
            imagePath
        )
        val documentName = email + "_${System.currentTimeMillis()}"

        saveDB = FirebaseFirestore.getInstance()

        saveDB.collection(USER_PATH).whereEqualTo("titlePath", imagePath).whereEqualTo("email",email).get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    saveDB.collection(USER_PATH).document(documentName).set(model)
                        .addOnSuccessListener {
                            toast("저장했습니다.")
                        }
                } else {
                    toast("이미 저장된 옷입니다.")
                }
            }
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

        categoryPath = intent.getStringExtra(CATEGORY_PATH).toString()
        imagePath = intent.getStringExtra(IMAGE_PATH).toString()
        email = FirebaseAuth.getInstance().currentUser?.email.toString()

        db = FirebaseFirestore.getInstance().collection(categoryPath)
        db.whereEqualTo("TitlePath", imagePath).get().await().documents.forEach { document ->
            document.toObject(ClothesModel::class.java)?.let { data ->
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

        val imageUrl = arrayListOf(clothesInfo.TitlePath)
        clothesInfo.ImageUrl?.map { imageUrl.add(it) }
        clothesViewpager.adapter = BannerAdapter(imageUrl)
        if (imageUrl.size == 1) {
            dotIndicator.hideUI()
        } else {
            dotIndicator.setViewPager2(clothesViewpager)
            dotIndicator.visibleUI()
        }
    }

    private fun setClothesInfo() = with(binding) {

        clothesInfo.let { info ->
            clothesShopTv.text = "쇼핑몰: ${info.Shop}"
            clothesNameTv.text = "옷이름: ${info.Name}"
            clothesInfoTv.text = info.Info
        }
        btnVisible()
    }

    private fun btnVisible() = with(binding){

        val sizeList = clothesInfo.Size

        if (sizeList != null) {
            for(size in sizeList){
                when(size){
                    "S" ->sBtn.visibleUI()
                    "M" ->mBtn.visibleUI()
                    "L" ->lBtn.visibleUI()
                    "XL" ->xlBtn.visibleUI()
                    "XXL" ->xxlBtn.visibleUI()

                }
            }
        }

    }

    override fun onDestroy() {
        coroutineContext.cancel()
        super.onDestroy()
    }

}