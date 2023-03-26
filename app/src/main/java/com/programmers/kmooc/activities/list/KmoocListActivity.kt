package com.programmers.kmooc.activities.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.programmers.kmooc.KmoocApplication
import com.programmers.kmooc.R
import com.programmers.kmooc.activities.detail.KmoocDetailActivity
import com.programmers.kmooc.databinding.ActivityKmookListBinding
import com.programmers.kmooc.viewmodels.KmoocListViewModel
import com.programmers.kmooc.viewmodels.KmoocListViewModelFactory

class KmoocListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKmookListBinding
    private lateinit var viewModel: KmoocListViewModel

    private val onClick: (String) -> Unit = {
        startDetailActivity(it)
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            (recyclerView.layoutManager as? LinearLayoutManager)?.run {
                val lastPosition = findLastCompletelyVisibleItemPosition() ?: 0

                if (lastPosition + 3 > itemCount && viewModel.isLoading.value == false) {
                    viewModel.next()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initLayout()
        initListener()
        initObserve()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding.lectureList.removeOnScrollListener(scrollListener)
    }

    private fun initData() {
        viewModel = ViewModelProvider(this, KmoocListViewModelFactory((application as KmoocApplication).kmoocRepository)).get(KmoocListViewModel::class.java)

        viewModel.list()
    }

    private fun initLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kmook_list)

        binding.lectureList.adapter = LecturesAdapter(onClick)
    }

    private fun initListener() {
        binding.pullToRefresh.setOnRefreshListener {
            viewModel.list()
        }
        binding.lectureList.addOnScrollListener(scrollListener)
    }

    private fun initObserve() {
        viewModel.isLoading.observe(this) {
            binding.progressBar.isVisible = it

            if (!it) {
                binding.pullToRefresh.isRefreshing = it
            }
        }
        viewModel.lectureList.observe(this) {
            (binding.lectureList.adapter as? LecturesAdapter)?.submitList(it)
        }
    }

    private fun startDetailActivity(id: String) {
        startActivity(
            Intent(this, KmoocDetailActivity::class.java)
                .apply { putExtra(KmoocDetailActivity.INTENT_PARAM_COURSE_ID, id) }
        )
    }
}
