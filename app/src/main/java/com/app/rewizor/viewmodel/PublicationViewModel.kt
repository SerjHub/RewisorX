package com.app.rewizor.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rewizor.data.RewizorError
import com.app.rewizor.data.model.PublicationDetailed
import com.app.rewizor.data.repository.PublicationRepository
import com.app.rewizor.exstension.asyncRequest

class PublicationViewModel(
    private val publicationRepository: PublicationRepository
) : BaseViewModel()
{

    private val publicationId: MutableLiveData<String> = MutableLiveData()

    private val publicationModel: MutableLiveData<PublicationDetailed> = MutableLiveData()
    val publicationModelLiveData: LiveData<PublicationDetailed> get() = publicationModel

    private val errorModel: MutableLiveData<String> = MutableLiveData()
    val errorModelLiveData: LiveData<String> get() = errorModel


    override fun onViewCreated() {
        asyncRequest(
            { publicationRepository.fetchPublication(publicationId.value!!) },
            { e -> showError(e) },
            { x -> showPublication(x!!) }

        )
    }

    private fun showPublication(p: PublicationDetailed?) {
        Log.i("FindPub", "${p == null}")
        publicationModel.value = p
    }

    private fun showError(error: RewizorError) {
        errorModel.value = error.message
    }

    fun setPublicationId(id: String) {
        publicationId.value = id
    }

}