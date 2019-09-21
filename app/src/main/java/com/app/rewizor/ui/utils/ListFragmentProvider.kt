package com.app.rewizor.ui.utils

import android.os.Bundle
import com.app.rewizor.ui.CategoryListFragment
import com.app.rewizor.ui.CategoryListFragment.Companion.CATEGORY_ID_ARG
import com.app.rewizor.ui.CategoryListFragment.Companion.CATEGORY_NAME_ARG
import com.app.rewizor.ui.model.FragmentParamsModel

object ListFragmentProvider {

    fun getTopicFragments(list: List<FragmentParamsModel>) =
        list.map {
            CategoryListFragment.getInstance().apply {
                arguments = Bundle().apply {
                    putString(CATEGORY_ID_ARG, it.categoryId)
                    putString(CATEGORY_NAME_ARG, it.categoryName)
                }
            }
        }
}