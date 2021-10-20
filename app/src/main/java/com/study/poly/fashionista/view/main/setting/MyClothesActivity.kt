package com.study.poly.fashionista.view.main.setting

import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.study.poly.fashionista.R
import com.study.poly.fashionista.base.BaseActivity
import com.study.poly.fashionista.data.ClothesSaveModel
import com.study.poly.fashionista.databinding.ActivityMyClothesBinding
import com.study.poly.fashionista.utility.Constant.CATEGORY_HOOD
import com.study.poly.fashionista.utility.Constant.CATEGORY_OUTER
import com.study.poly.fashionista.utility.Constant.CATEGORY_PANTS
import com.study.poly.fashionista.utility.Constant.CATEGORY_T_SHIRT
import com.study.poly.fashionista.utility.Constant.USER_PATH
import com.study.poly.fashionista.utility.hideUI
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.CoroutineContext

class MyClothesActivity :
    BaseActivity<ActivityMyClothesBinding>({ ActivityMyClothesBinding.inflate(it) }),
    CoroutineScope {


    private lateinit var db: CollectionReference
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private var clothesInfo = ArrayList<ClothesSaveModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        getData()
    }

    private fun initView() = with(binding) {

        layoutShirt.apply {
            clothesRecyclerview.adapter
            btnMoreClothes.hideUI()
        }
        layoutHood.apply {
            clothesRecyclerview.adapter
            btnMoreClothes.hideUI()
        }
        layoutOvercoat.apply {
            clothesRecyclerview.adapter
            btnMoreClothes.hideUI()
        }
        layoutPants.apply {
            clothesRecyclerview.adapter
            btnMoreClothes.hideUI()
        }
    }

    private fun getData() {

        launch {
            getMyClothesInfo()
        }
    }

    private suspend fun getMyClothesInfo() = withContext(Dispatchers.IO) {
        val email = FirebaseAuth.getInstance().currentUser?.email
        db = FirebaseFirestore.getInstance().collection(USER_PATH)
        db.whereEqualTo("email", email).get().await().documents.forEach { document ->
            document.toObject(ClothesSaveModel::class.java)?.let { data ->
                clothesInfo.add(data)
            }
            getCategoryInfo()
        }

    }

    private fun getCategoryInfo() {

        for (i in clothesInfo.indices) {

            when (clothesInfo[i].categoryPath) {
                CATEGORY_HOOD -> {

                }
                CATEGORY_OUTER -> {

                }
                CATEGORY_PANTS -> {

                }
                CATEGORY_T_SHIRT -> {

                }
            }
        }
    }
}