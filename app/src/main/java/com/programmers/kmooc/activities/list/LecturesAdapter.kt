package com.programmers.kmooc.activities.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.programmers.kmooc.R
import com.programmers.kmooc.databinding.ViewKmookListItemBinding
import com.programmers.kmooc.models.LectureSimple

class LecturesAdapter(private val onClick: (String) -> Unit) : ListAdapter<LectureSimple, LecturesAdapter.LectureViewHolder>(LectureDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_kmook_list_item, parent, false)
        val binding = ViewKmookListItemBinding.bind(view)
        return LectureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LectureViewHolder(private val binding: ViewKmookListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lecture: LectureSimple) {
            binding.lecture = lecture

            itemView.setOnClickListener {
                onClick(lecture.id)
            }
        }
    }
}

class LectureDiffUtil : DiffUtil.ItemCallback<LectureSimple>() {
    override fun areItemsTheSame(oldItem: LectureSimple, newItem: LectureSimple): Boolean = oldItem === newItem

    override fun areContentsTheSame(oldItem: LectureSimple, newItem: LectureSimple): Boolean = oldItem == newItem
}