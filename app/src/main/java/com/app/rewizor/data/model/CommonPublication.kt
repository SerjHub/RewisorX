package com.app.rewizor.data.model

/** Node info in api */
data class CommonPublication(
    val id: String,
    val category: String,
    val name: String,
    val description: String?,
    val date: String?,
    val end: String?,
    val age: Int?,
    val address: String,
    val image: ImageInfo,
    val votes: Int,
    val rating: Int,
    val comments: Int,
    val views: Int,
    val likes: Int,
    val hasLike: Boolean,
    var categoryTitle: String? = ""
)