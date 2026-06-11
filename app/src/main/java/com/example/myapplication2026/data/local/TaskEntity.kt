package com.example.myapplication2026.data.local

data class TaskEntity(
    val id: Int = 0,
    val title: String,
    val description: String,
    val category: String,
    val isDone: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)