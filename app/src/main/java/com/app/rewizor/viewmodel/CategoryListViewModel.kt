package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.app.rewizor.data.model.PublicationCommon
import com.app.rewizor.data.repository.PublicationRepository
import com.app.rewizor.exstension.asyncRequest
import com.app.rewizor.viewmodel.livedata.SingleLiveEvent

class CategoryListViewModel(
    private val publicationsRepository: PublicationRepository
) : BaseViewModel() {

    private lateinit var parentViewModel: TopicViewModel
    private var currentPage = START_PAGE

    private val filter
        get() = parentViewModel.filterStateLiveData.value

    private val currentCategoryId: MutableLiveData<String> = MutableLiveData()

    private val publicationList: MutableLiveData<List<PublicationCommon>> = MutableLiveData()
    val publicationListLiveData: LiveData<List<PublicationCommon>> get() = publicationList

    private val refreshList: MutableLiveData<Boolean> = SingleLiveEvent()
    val refreshListLiveData: MutableLiveData<Boolean> get() = refreshList

    private val loadingSate: MutableLiveData<Boolean> = MutableLiveData()
    val loadingStateLiveData: LiveData<Boolean> get() = loadingSate

    private val updateObserver = Observer<Boolean> { u ->
        if (u) onRefresh()
    }

    fun setUpModelParams(topicViewModel: TopicViewModel, categoryId: String) {
        parentViewModel = topicViewModel
        this.currentCategoryId.value = categoryId
        observeFilterUpdate()
    }

    private fun observeFilterUpdate() {
        parentViewModel.updateCategoryLiveData.observeForever(updateObserver)
    }

    override fun onViewCreated() {
        onRefresh()
    }

    private fun prepareParams(): PublicationRepository.Params {
        return PublicationRepository.Params(
            currentPage,
            PAGE_SIZE,
            currentCategoryId.value!!,
            parentViewModel.fragmentsTopicLiveData.value!!.requestKey
        )
    }

    private fun prepareFilter(): PublicationRepository.Filter {
        return filter!!.run {
            PublicationRepository.Filter(
                age,
                dates,
                searchText,
                place,
                popular,
                recommend
                )
        }
    }

    private fun onNextPage() {
        if (loadingSate.value != true) {
            asyncRequest(
                loadData = {
                    requestParams(currentCategoryId.value!!)!!
                        .run {
                            publicationsRepository.fetchPublicationsList(
                                prepareParams(),
                                prepareFilter()
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

    private fun onRefresh() {
        refreshList.value = true
        currentPage = START_PAGE
        onNextPage()
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

    override fun onCleared() {
        super.onCleared()
        parentViewModel.updateCategoryLiveData.removeObserver(updateObserver)
    }


    companion object {
        const val PAGE_SIZE = 10
        const val START_PAGE = 1
    }
}