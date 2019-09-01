package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.model.CommonPublication
import com.app.rewizor.data.repository.PublicationRepository
import com.app.rewizor.ui.utils.TOPIC
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent

class CategoryListViewModel(
    private val publicationsRepository: PublicationRepository
) : BaseViewModel() {
    private lateinit var parentViewModel: TopicViewModel
    private var currentPage = START_PAGE

    private val currentCategoryId: MutableLiveData<String> = MutableLiveData()

    private val publicationList: MutableLiveData<List<CommonPublication>> = MutableLiveData()
    val publicationListLiveData: LiveData<List<CommonPublication>> get() = publicationList

    private val refreshList: MutableLiveData<Boolean> = SingleLiveEvent()
    val refreshListLiveData: MutableLiveData<Boolean> get() = refreshList

    private val loadingSate: MutableLiveData<Boolean> = MutableLiveData()
    val loadingStateLiveData: LiveData<Boolean> get() = loadingSate

    fun setUpModelParams(topicViewModel: TopicViewModel, categoryId: String) {
        parentViewModel = topicViewModel
        this.currentCategoryId.value = categoryId
    }

    override fun onViewCreated() {
        onRefresh()
        onNextPage()
    }

    fun onNextPage() {
        if (loadingSate.value != true) {
            with(asyncProvider) {
                startSuspend {
                    loadingSate.value = true
                    val listResult = requestParams(currentCategoryId.value!!)!!.run {
                        executeBackGroundTask { publicationsRepository.fetchPublicationsList(
                            currentPage,
                            PAGE_SIZE,
                            currentCategoryId.value!!,
                            TOPIC.valueOf(topic).requestKey
                        ) }
                    }
                    if (!listResult.isError) {
                        onPageLoaded(listResult.model)
                    }

                    loadingSate.value = false
                }
            }
        }
    }

    fun onRefresh() {
        refreshList.value = true
        currentPage = START_PAGE
    }

    fun listScrolled(position: Int) {
        publicationList.value?.let {
            if (it.size - position < 3) onNextPage()
        }
    }

    private fun onPageLoaded(list: List<CommonPublication>) {
        currentPage += 1
        publicationList.value = list
    }

    private fun requestParams(categoryId: String) =
        parentViewModel.getParams(categoryId)



    companion object {
        const val PAGE_SIZE = 10
        const val START_PAGE = 1
    }
}