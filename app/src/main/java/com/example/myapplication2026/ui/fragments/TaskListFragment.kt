package com.example.myapplication2026.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication2026.R
import com.example.myapplication2026.adapter.TaskAdapter
import com.example.myapplication2026.data.local.TaskEntity
import com.example.myapplication2026.databinding.FragmentTaskListBinding
import com.example.myapplication2026.ui.viewmodel.TaskViewModel

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupAddButton()
        observeTasks()
    }

    override fun onResume() {
        super.onResume()
        taskViewModel.loadTasks()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            onEditClick = { task ->
                val bundle = bundleOf("taskId" to task.id)

                findNavController().navigate(
                    R.id.action_taskListFragment_to_addTaskFragment,
                    bundle
                )
            },
            onDeleteClick = { task ->
                taskViewModel.deleteTask(task)
                taskViewModel.loadTasks()
            },
            onShareClick = { task ->
                shareTask(task)
            }
        )

        binding.rvTasks.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupAddButton() {
        binding.fabAddTask.setOnClickListener {
            findNavController().navigate(
                R.id.action_taskListFragment_to_addTaskFragment
            )
        }
    }

    private fun observeTasks() {
        taskViewModel.allTasks.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.submitList(tasks)

            if (tasks.isEmpty()) {
                binding.emptyStateCard.visibility = View.VISIBLE
                binding.rvTasks.visibility = View.GONE
            } else {
                binding.emptyStateCard.visibility = View.GONE
                binding.rvTasks.visibility = View.VISIBLE
            }
        }
    }

    private fun shareTask(task: TaskEntity) {
        val shareText = """
            Task: ${task.title}
            Description: ${task.description}
            Category: ${task.category}
        """.trimIndent()

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        startActivity(
            Intent.createChooser(
                shareIntent,
                "Share task"
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}