package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.model.PublicationCommon
import com.app.rewizor.data.repository.PublicationRepository
import com.app.rewizor.exstension.asyncRequest
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent

class CategoryListViewModel(
    private val publicationsRepository: PublicationRepository
) : BaseViewModel() {
    private lateinit var parentViewModel: TopicViewModel
    private var currentPage = START_PAGE

    private val currentCategoryId: MutableLiveData<String> = MutableLiveData()

    private val publicationList: MutableLiveData<List<PublicationCommon>> = MutableLiveData()
    val publicationListLiveData: LiveData<List<PublicationCommon>> get() = publicationList

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

    private fun prepareParams(): PublicationRepository.Params {
        requestParams(currentCategoryId.value!!)!!
            .run {

            }
        return PublicationRepository.Params(
            currentPage,
            PAGE_SIZE,
            currentCategoryId.value!!,
            parentViewModel.fragmentsTopicLiveData.value!!.requestKey
        )
    }

    fun onNextPage() {
        if (loadingSate.value != true) {
            asyncRequest(
                loadData = {
                    requestParams(currentCategoryId.value!!)!!
                        .run {
                            publicationsRepository.fetchPublicationsList(
                                prepareParams()
                            )
                        }
                },
                onFail = {  },
                onSuccess = { it?.let { onPageLoaded(it) } },
                postOnStart = { loadingSate.value == true },
                postOnFinish = { loadingSate.value = false }
            )
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

    private fun onPageLoaded(list: List<PublicationCommon>) {
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