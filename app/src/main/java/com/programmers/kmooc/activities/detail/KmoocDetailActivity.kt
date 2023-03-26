package com.programmers.kmooc.activities.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.programmers.kmooc.KmoocApplication
import com.programmers.kmooc.R
import com.programmers.kmooc.databinding.ActivityKmookDetailBinding
import com.programmers.kmooc.viewmodels.KmoocDetailViewModel
import com.programmers.kmooc.viewmodels.KmoocDetailViewModelFactory

class KmoocDetailActivity : AppCompatActivity() {

    companion object {
        const val INTENT_PARAM_COURSE_ID = "param_course_id"
    }

    private val binding: ActivityKmookDetailBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_kmook_detail) }

    private lateinit var viewModel: KmoocDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initLayout()
        initListener()
        initObserve()
    }

    private fun initData() {
        val kmoocRepository = (application as KmoocApplication).kmoocRepository
        viewModel = ViewModelProvider(this, KmoocDetailViewModelFactory(kmoocRepository)).get(
            KmoocDetailViewModel::class.java
        )

        intent.getStringExtra(INTENT_PARAM_COURSE_ID)?.let {
            viewModel.detail(it)
        } ?: kotlin.run {
            finish()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initLayout() {

    }

    private fun initListener() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initObserve() {
        viewModel.isLoading.observe(this) {
            binding.progressBar.isVisible = it
        }

        viewModel.lectureDetail.observe(this) {
            binding.toolbar.title = it.name

            binding.lectureDue.setDescription("운영 기간", "%s ~ %s".format(it.start, it.end))
            binding.lectureNumber.setDescription("강좌 번호", it.number)
            binding.lectureTeachers.setDescription("교수 정보", it.teachers)
            binding.lectureType.setDescription("강좌 분류", "%s/%s".format(it.classfyName, it.middleClassfyName))
            binding.lectureOrg.setDescription("운영 기관", it.orgName)
        }
    }
}