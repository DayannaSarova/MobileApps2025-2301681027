package com.example.myapplication2026.data.repository

import android.content.Context
import com.example.myapplication2026.data.local.TaskDatabaseHelper
import com.example.myapplication2026.data.local.TaskEntity

class TaskRepository(context: Context) {

    private val databaseHelper = TaskDatabaseHelper(context)

    fun insertTask(task: TaskEntity): Long {
        return databaseHelper.insertTask(task)
    }

    fun getAllTasks(): List<TaskEntity> {
        return databaseHelper.getAllTasks()
    }

    fun updateTask(task: TaskEntity): Int {
        return databaseHelper.updateTask(task)
    }

    fun deleteTask(taskId: Int): Int {
        return databaseHelper.deleteTask(taskId)
    }

    fun getTaskById(taskId: Int): TaskEntity? {
        return databaseHelper.getTaskById(taskId)
    }
}