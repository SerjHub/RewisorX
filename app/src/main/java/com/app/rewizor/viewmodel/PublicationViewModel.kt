package com.app.rewizor.viewmodel

import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.repository.PublicationRepository

class PublicationViewModel(
    private val publicationRepository: PublicationRepository
) : BaseViewModel()
{

    private val publicationId: MutableLiveData<Long> = MutableLiveData()



    override fun onViewCreated() {
        with(asyncProvider) {
            startSuspend {
               // val publicationResult = publicationRepository.fetchPublicationsList()
            }
        }
    }

    fun setPublicationId(id: Long) {
        publicationId.value = id
    }

}