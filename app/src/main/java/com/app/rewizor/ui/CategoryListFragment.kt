package com.app.rewizor.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.app.rewizor.MainActivity
import com.app.rewizor.PublicationActivity
import com.app.rewizor.PublicationActivity.Companion.ID_INTENT_KEY
import com.app.rewizor.PublicationActivity.Companion.PARENT_INTENT_KEY
import com.app.rewizor.R
import com.app.rewizor.data.model.PublicationCommon
import com.app.rewizor.ui.adapter.PublicationsAdapter
import com.app.rewizor.ui.utils.TOPIC
import com.app.rewizor.ui.utils.TOPIC_KEY
import com.app.rewizor.viewmodel.CategoryListViewModel
import kotlinx.android.synthetic.main.fragment_publications_list.*
import org.koin.android.ext.android.inject


/*Театры, кино, литература ....*/
class CategoryListFragment : BaseFragment() {
    override val layout = R.layout.fragment_publications_list
    private val viewModel: CategoryListViewModel by inject()

    private val topicParam
        get() = (parentFragment as TopicTabFragment).arguments?.getString(TOPIC_KEY)!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setViewModel(viewModel)
    }

    private fun setAdapter() {
        publicationsRecyclerView.adapter =
            PublicationsAdapter(TOPIC.valueOf(topicParam),
                { viewModel.listScrolled(it) },
                {
                    openPublication(it)
                }
            )
    }

    private fun openPublication(id: String) {
        (activity as MainActivity)
            .also {
                startActivity(
                    Intent(it, PublicationActivity::class.java).apply {
                        putExtra(ID_INTENT_KEY, id)
                        putExtra(PARENT_INTENT_KEY, topicParam)
                    }
                )
            }
    }

    private fun setViewModel(viewModel: CategoryListViewModel) {
        with(viewModel) {
            setUpModelParams(
                (parentFragment as TopicTabFragment).viewModel,
                arguments?.getString(CATEGORY_ID_ARG)!!
            )
            publicationListLiveData.observe(viewLifecycleOwner, Observer { updateAdapterDate(it) })
            refreshListLiveData.observe(viewLifecycleOwner, Observer { refreshList() })
            onViewCreated()
        }
    }

    private fun refreshList() {
        (publicationsRecyclerView.adapter as PublicationsAdapter)
            .also {
                it.refreshList()
            }
    }

    private fun updateAdapterDate(list: List<PublicationCommon>) {
        (publicationsRecyclerView.adapter as PublicationsAdapter)
            .also {
                it.updateItems(list)
            }
    }


    override val TAG = "CategoryListFragment"

    companion object {
        const val CATEGORY_ID_ARG = "categoryId_arg"
        const val CATEGORY_NAME_ARG = "categoryName_arg"
        fun getInstance(bundle: Bundle? = null) = CategoryListFragment().apply {
            arguments = bundle
        }
    }

}