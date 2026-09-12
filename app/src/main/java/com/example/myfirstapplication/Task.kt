package com.example.myfirstapplication

data class Task (
    val id: Long = System.currentTimeMillis(),
    val title: String,
    var isCompleted: Boolean = false
)