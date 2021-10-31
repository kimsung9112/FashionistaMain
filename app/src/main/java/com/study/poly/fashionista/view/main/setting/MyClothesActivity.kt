package com.study.poly.fashionista.view.main.setting

import android.content.Intent
import android.os.Bundle

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

import com.study.poly.fashionista.R
import com.study.poly.fashionista.base.BaseActivity
import com.study.poly.fashionista.data.ClothesSaveModel
import com.study.poly.fashionista.databinding.ActivityMyClothesBinding
import com.study.poly.fashionista.utility.*
import com.study.poly.fashionista.utility.Constant.CATEGORY_HOOD
import com.study.poly.fashionista.utility.Constant.CATEGORY_OUTER
import com.study.poly.fashionista.utility.Constant.CATEGORY_PANTS
import com.study.poly.fashionista.utility.Constant.CATEGORY_T_SHIRT
import com.study.poly.fashionista.utility.Constant.USER_PATH
import com.study.poly.fashionista.view.adapter.MainClothesAdapter
import com.study.poly.fashionista.view.main.home.ClothesDetailActivity

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

import java.lang.Exception
import java.net.UnknownHostException

class MyClothesActivity :
    BaseActivity<ActivityMyClothesBinding>({ ActivityMyClothesBinding.inflate(it) }),
    CoroutineScope {

    private lateinit var db: CollectionReference
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private var clothesInfo = ArrayList<ClothesSaveModel>()
    private val hoodList = ArrayList<String>()
    private val outerList = ArrayList<String>()
    private val pantsList = ArrayList<String>()
    private val tShirtList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        getData()
    }

    private fun initView() = with(binding) {
        titleLayout.titleTv.text = "내 옷장"
        titleLayout.btnBack.onThrottleFirstClick { onBackPressed() }
        layoutShirt.apply {
            clothesName.text = getString(R.string.main_clothes_t_shirt)
            btnMoreClothes.hideUI()
        }
        layoutHood.apply {
            clothesName.text = getString(R.string.main_clothes_hood)
            btnMoreClothes.hideUI()
        }
        layoutOvercoat.apply {
            clothesName.text = getString(R.string.main_clothes_outer)
            btnMoreClothes.hideUI()
        }
        layoutPants.apply {
            clothesName.text = getString(R.string.main_clothes_pants)
            btnMoreClothes.hideUI()
        }
        notNetworkLayout.refreshBtn.onThrottleFirstClick {
            notNetworkLayout.root.hideUI()
            getData()
        }
    }

    private fun getData() = with(binding) {
        launch {
            try {
                getMyClothesInfo()
            } catch (e: UnknownHostException) {
                notNetworkLayout.root.visibleUI()
            } catch (e: Exception) {
                toast(e.message.toString())
            }
        }
    }

    private suspend fun showProgress() = withContext(coroutineContext) {
        binding.loadingBar.root.visibleUI()
    }

    private suspend fun dismissProgress() = withContext(coroutineContext) {
        binding.loadingBar.root.hideUI()
    }

    private suspend fun getMyClothesInfo() = withContext(Dispatchers.IO) {
        showProgress()

        val email = FirebaseAuth.getInstance().currentUser?.email
        db = FirebaseFirestore.getInstance().collection(USER_PATH)
        db.whereEqualTo("email", email).get().await().documents.map { document ->
            async {
                document.toObject(ClothesSaveModel::class.java)?.let { data ->
                    clothesInfo.add(data)
                }
            }
        }.awaitAll()

        getCategoryInfo()
    }

    private suspend fun getCategoryInfo() = with(Dispatchers.IO) {
        clothesInfo.map {
            async {
                when (it.categoryPath) {
                    CATEGORY_HOOD -> {
                        hoodList.add(it.titlePath)
                    }
                    CATEGORY_OUTER -> {
                        outerList.add(it.titlePath)
                    }
                    CATEGORY_PANTS -> {
                        pantsList.add(it.titlePath)
                    }
                    CATEGORY_T_SHIRT -> {
                        tShirtList.add(it.titlePath)
                    }
                    else -> {
                        return@async
                    }
                }
            }
        }.awaitAll()
        withContext(Dispatchers.Main) {
            if (!listEmptyCheck()) {
                setRecyclerview()
            } else {
                binding.tvNoneData.visibleUI()
            }
            dismissProgress()
        }
    }

    private fun setRecyclerview() = with(binding) {
        val intent = Intent(this@MyClothesActivity, ClothesDetailActivity::class.java)

        layoutShirt.clothesRecyclerview.let { list ->
            if (tShirtList.isNotEmpty()) {
                layoutShirt.root.visibleUI()
            }
            list.adapter = MainClothesAdapter(tShirtList) { url ->
                intent.putExtra(ClothesDetailActivity.CATEGORY_PATH, CATEGORY_T_SHIRT)
                intent.putExtra(ClothesDetailActivity.IMAGE_PATH, url)
                moveNext(intent)
            }
        }
        layoutHood.clothesRecyclerview.let { list ->
            if (hoodList.isNotEmpty()) {
                layoutHood.root.visibleUI()
            }
            list.adapter = MainClothesAdapter(hoodList) { url ->
                intent.putExtra(ClothesDetailActivity.CATEGORY_PATH, CATEGORY_HOOD)
                intent.putExtra(ClothesDetailActivity.IMAGE_PATH, url)
                moveNext(intent)
            }
        }
        layoutOvercoat.clothesRecyclerview.let { list ->
            if (outerList.isNotEmpty()) {
                layoutOvercoat.root.visibleUI()
            }
            list.adapter = MainClothesAdapter(outerList) { url ->
                intent.putExtra(ClothesDetailActivity.CATEGORY_PATH, CATEGORY_OUTER)
                intent.putExtra(ClothesDetailActivity.IMAGE_PATH, url)
                moveNext(intent)
            }
        }
        layoutPants.clothesRecyclerview.let { list ->
            if (pantsList.isNotEmpty()) {
                layoutPants.root.visibleUI()
            }
            list.adapter = MainClothesAdapter(pantsList) { url ->
                intent.putExtra(ClothesDetailActivity.CATEGORY_PATH, CATEGORY_PANTS)
                intent.putExtra(ClothesDetailActivity.IMAGE_PATH, url)
                moveNext(intent)
            }
        }
    }

    private fun listEmptyCheck() = clothesInfo.isEmpty()

    private fun moveNext(intent: Intent) {
        startActivity(intent)
        moveNextAnim()
    }
    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}