package com.study.poly.fashionista.view.main.home

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.study.poly.fashionista.base.BaseActivity
import com.study.poly.fashionista.data.ClothesModel
import com.study.poly.fashionista.databinding.ActivityClothesMoreBinding
import com.study.poly.fashionista.utility.*
import com.study.poly.fashionista.view.adapter.MoreClothesAdapter
import com.study.poly.fashionista.view.dialog.IBmiResult
import com.study.poly.fashionista.view.dialog.MyClothesSizeDialog
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class ClothesMoreActivity :
    BaseActivity<ActivityClothesMoreBinding>({ ActivityClothesMoreBinding.inflate(it) }),
    IBmiResult,
    CoroutineScope {

    companion object {
        const val CLOTHES_TYPE = "clothes_type"
    }

    private lateinit var db: CollectionReference
    private val clothesList = ArrayList<ClothesModel>()
    private var path: String = ""
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewInit()
    }

    private fun viewInit() = with(binding) {

        val titleName = intent.getStringExtra(CLOTHES_TYPE)

        path = when (titleName) {

            ClothesType.HOOD.clothes -> {
                "HOOD_INFO"
            }
            ClothesType.OUTER.clothes -> {
                "OUTER_INFO"
            }
            ClothesType.PANTS.clothes -> {
                "PANTS_INFO"
            }
            else -> {
                "T_SHIRT_INFO"
            }
        }

        titleLayout.titleTv.text = titleName
        titleLayout.btnBack.setOnClickListener { onBackPressed() }

        db = FirebaseFirestore.getInstance().collection(path)

        notNetworkLayout.refreshBtn.onThrottleFirstClick {
            notNetworkLayout.root.hideUI()
            getData()
        }
        getData()
        dialogShow()
    }

    private fun getData() {

        launch {
            try {
                showProgress()
                getList()
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

    private suspend fun getList() = withContext(Dispatchers.IO) {

        clothesList.clear()

        db.get().await().documents.forEach { document ->
            document.toObject(ClothesModel::class.java)?.let { data ->
                clothesList.add(data)
            }
        }.also {
            withContext(Dispatchers.Main) {
                setRecyclerView()
                dismissProgress()
            }
        }
    }

    private fun setRecyclerView() = with(binding) {

        val nextIntent = Intent(this@ClothesMoreActivity, ClothesDetailActivity::class.java)

        clothesRecyclerview.let { list ->
            list.layoutManager = LinearLayoutManager(this@ClothesMoreActivity)
            list.adapter = MoreClothesAdapter(clothesList) { url ->
                nextIntent.putExtra(ClothesDetailActivity.CATEGORY_PATH, path)
                nextIntent.putExtra(ClothesDetailActivity.IMAGE_PATH, url)
                startActivity(nextIntent)
                moveNextAnim()
            }
        }
    }

    private fun dialogShow() {

        val dialog = MyClothesSizeDialog(this,this)

        binding.btnSizeGet.onThrottleFirstClick {
            dialog.show()
        }

    }

    override fun getSize(bmiSize: String) {
    }

    override fun onDestroy() {
        coroutineContext.cancel()
        super.onDestroy()
    }
}

