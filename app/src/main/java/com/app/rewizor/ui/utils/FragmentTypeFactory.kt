package com.app.rewizor.ui.utils

import android.os.Bundle
import com.app.rewizor.ui.CategoryListFragment

object FragmentTypeFactory {
    const val CATEGORY_KEY = "category"

    fun getTopicFragments(param: TOPIC) =
        listOf(
            CategoryListFragment.getInstance(Bundle().apply { putString(CATEGORY_KEY, CATEGORY.ALL.categoryTitle) }),
            CategoryListFragment.getInstance(Bundle().apply { putString(CATEGORY_KEY, CATEGORY.THEATRE.categoryTitle) }),
            CategoryListFragment.getInstance(Bundle().apply { putString(CATEGORY_KEY, CATEGORY.LITERATURE.categoryTitle) }),
            CategoryListFragment.getInstance(Bundle().apply { putString(CATEGORY_KEY, CATEGORY.ALL.categoryTitle) }),
            CategoryListFragment.getInstance(Bundle().apply { putString(CATEGORY_KEY, CATEGORY.ALL.categoryTitle) })
        )


}