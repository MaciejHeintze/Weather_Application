package com.example.weatherapplication.data.db.database


data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String,
    val icon: String
)