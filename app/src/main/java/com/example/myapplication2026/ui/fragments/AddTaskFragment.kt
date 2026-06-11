package com.example.myapplication2026.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication2026.data.local.TaskEntity
import com.example.myapplication2026.databinding.FragmentAddTaskBinding
import com.example.myapplication2026.ui.viewmodel.TaskViewModel

class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel: TaskViewModel by viewModels()

    private var taskId: Int = -1
    private var currentTask: TaskEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskId = arguments?.getInt("taskId", -1) ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupScreen()
        setupButtons()
    }

    private fun setupScreen() {
        if (taskId == -1) {
            binding.tvScreenTitle.text = "Add Task"
            binding.btnSaveTask.text = "Save Task"
        } else {
            binding.tvScreenTitle.text = "Edit Task"
            binding.btnSaveTask.text = "Update Task"

            currentTask = taskViewModel.getTaskById(taskId)

            if (currentTask == null) {
                Toast.makeText(requireContext(), "Task not found", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                return
            }

            currentTask?.let { task ->
                binding.etTaskTitle.setText(task.title)
                binding.etTaskDescription.setText(task.description)
                binding.etTaskCategory.setText(task.category)
            }
        }
    }

    private fun setupButtons() {
        binding.btnSaveTask.setOnClickListener {
            saveTask()
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun saveTask() {
        val title = binding.etTaskTitle.text.toString().trim()
        val description = binding.etTaskDescription.text.toString().trim()
        val category = binding.etTaskCategory.text.toString().trim()

        if (title.isEmpty()) {
            binding.etTaskTitle.error = "Title is required"
            return
        }

        if (description.isEmpty()) {
            binding.etTaskDescription.error = "Description is required"
            return
        }

        if (category.isEmpty()) {
            binding.etTaskCategory.error = "Category is required"
            return
        }

        if (taskId == -1) {
            val newTask = TaskEntity(
                title = title,
                description = description,
                category = category
            )

            taskViewModel.insertTask(newTask)

            Toast.makeText(
                requireContext(),
                "Task added",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val oldTask = currentTask

            if (oldTask != null) {
                val updatedTask = oldTask.copy(
                    title = title,
                    description = description,
                    category = category
                )

                taskViewModel.updateTask(updatedTask)

                Toast.makeText(
                    requireContext(),
                    "Task updated",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}