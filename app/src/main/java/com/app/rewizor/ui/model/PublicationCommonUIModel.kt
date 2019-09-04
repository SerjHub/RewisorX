package com.app.rewizor.ui.model

import com.app.rewizor.data.model.ImageInfo
import com.app.rewizor.data.model.PublicationCommon
import com.app.rewizor.data.model.Region
import com.app.rewizor.ui.utils.DatePrinter

data class PublicationCommonUIModel(
    val guid: String,
    val category: String,
    val name: String,
    val description: String?,
    val date: String?,
    val age: Int?,
    val address: String?,
    val tagSuffix: String?,
    val parentAddress: String?,
    val parentName: String?,
    val parentRegion: Region?,
    val image: ImageInfo,
    val votes: Int,
    val rating: Int,
    val comments: Int,
    val views: Int,
    val likes: Int,
    val hasLike: Boolean,
    var categoryTitle: String? = ""
)

fun PublicationCommon.map(p: PublicationCommon) =
    p.run {
        PublicationCommonUIModel(
guid,
            category,
            name,
            description,
            DatePrinter.getDateForAdapter(date, end),
            age,
            address,
            "",
            parentAddress,
            parentName,
            parentRegion,
            image,
            votes,
            rating,
            comments,
            views,
            likes,
            hasLike

        )
    }
