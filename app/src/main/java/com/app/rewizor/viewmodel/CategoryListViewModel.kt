package com.app.rewizor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.repository.PublicationRepository

class CategoryListViewModel(
    private val publicationsRepository: PublicationRepository
) : BaseViewModel() {
    private var currentPage = 0
    private val topicId: MutableLiveData<String> = MutableLiveData()
    private val categoryId: MutableLiveData<String> = MutableLiveData()

    private val publicationList: MutableLiveData<List<String>> = MutableLiveData()
    val publicationListLiveData: LiveData<List<String>> get() = publicationList

    private val loadingSate: MutableLiveData<Boolean> = MutableLiveData()
    val loadingStateLiveData: LiveData<Boolean> get() = loadingSate

    fun setUpModelParams(topic: String, category: String){
        topicId.value = topic
        categoryId.value = category
    }

    override fun onViewCreated() {
        onNextPage()
    }

    fun onNextPage() {
        if (loadingSate.value != true) {
            with(asyncProvider) {
                startSuspend {
                    executeBackGroundTask { publicationsRepository.getPublicationsList(
                        currentPage,
                        PAGE_SIZE,
                        topicId.value!!,
                        categoryId.value!!
                    ) }
                }
            }
        }
    }



    companion object {
        const val PAGE_SIZE = 10
    }
}