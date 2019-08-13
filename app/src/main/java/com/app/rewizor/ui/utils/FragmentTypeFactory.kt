package com.app.rewizor.ui.utils

import android.os.Bundle
import com.app.rewizor.ui.CategoryListFragment
import com.app.rewizor.ui.TopicTabFragment

object FragmentTypeFactory {

    fun getTopicFragments() =
        listOf(
            CategoryListFragment.getInstance(Bundle().apply { putString(CATEGORY_KEY, CATEGORY.ALL.categoryTitle) }),
            CategoryListFragment.getInstance(Bundle().apply { putString(CATEGORY_KEY, CATEGORY.THEATRE.categoryTitle) }),
            CategoryListFragment.getInstance(Bundle().apply { putString(CATEGORY_KEY, CATEGORY.LITERATURE.categoryTitle) }),
            CategoryListFragment.getInstance(Bundle().apply { putString(CATEGORY_KEY, CATEGORY.MOVIE.categoryTitle) })
      //      CategoryListFragment.getInstance(Bundle().apply { putString(CATEGORY_KEY, CATEGORY.ALL.categoryTitle) })
        )

    fun getTabFragment(topic: TOPIC) =
        TopicTabFragment.getInstance()
}