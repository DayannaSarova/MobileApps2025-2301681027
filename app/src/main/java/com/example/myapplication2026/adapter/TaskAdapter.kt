package com.example.myapplication2026.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication2026.data.local.TaskEntity
import com.example.myapplication2026.databinding.ItemTaskBinding

class TaskAdapter(
    private val onEditClick: (TaskEntity) -> Unit,
    private val onDeleteClick: (TaskEntity) -> Unit,
    private val onShareClick: (TaskEntity) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var tasks: List<TaskEntity> = emptyList()

    fun submitList(newTasks: List<TaskEntity>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TaskEntity) {
            binding.tvTaskTitle.text = task.title
            binding.tvTaskDescription.text = task.description
            binding.tvTaskCategory.text = task.category

            binding.btnEditTask.setOnClickListener {
                onEditClick(task)
            }

            binding.btnDeleteTask.setOnClickListener {
                onDeleteClick(task)
            }

            binding.btnShareTask.setOnClickListener {
                onShareClick(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}