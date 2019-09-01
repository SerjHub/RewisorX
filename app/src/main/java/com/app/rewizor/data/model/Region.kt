package com.app.rewizor.data.model

data class Region(
    val id: Int,
    val name: String
  //  var isFirst: Boolean = false
) {
    companion object {
        val DEFAULT = Region(0, "")
    }
}