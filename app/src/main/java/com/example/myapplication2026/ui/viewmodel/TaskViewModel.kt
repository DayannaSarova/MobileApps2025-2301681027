package com.example.myapplication2026.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication2026.data.local.TaskEntity
import com.example.myapplication2026.data.repository.TaskRepository

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TaskRepository(application)

    private val _allTasks = MutableLiveData<List<TaskEntity>>()
    val allTasks: LiveData<List<TaskEntity>> = _allTasks

    init {
        loadTasks()
    }

    fun loadTasks() {
        _allTasks.value = repository.getAllTasks()
    }

    fun insertTask(task: TaskEntity) {
        repository.insertTask(task)
        loadTasks()
    }

    fun updateTask(task: TaskEntity) {
        repository.updateTask(task)
        loadTasks()
    }

    fun deleteTask(task: TaskEntity) {
        repository.deleteTask(task.id)
        loadTasks()
    }

    fun getTaskById(taskId: Int): TaskEntity? {
        return repository.getTaskById(taskId)
    }
}