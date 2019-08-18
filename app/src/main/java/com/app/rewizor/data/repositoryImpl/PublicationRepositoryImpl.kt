package com.app.rewizor.data.repositoryImpl

import android.util.Log
import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.CommonPublication
import com.app.rewizor.data.model.PageInfo
import com.app.rewizor.data.repository.PublicationRepository
import com.app.rewizor.remote.RestClient

class PublicationRepositoryImpl (
    private val apiClient: RestClient
) : PublicationRepository {


    override suspend fun getMainPublicationsList(
        page: Int,
        pageCount: Int,
        topic: String,
        category: String?
    ): RewizorResult<List<CommonPublication>> {
        val publicationsResult = apiClient.run { callApi { api.getNodes() } }
        Log.i("FindPage", "$publicationsResult")
        val pageResult = publicationsResult.map(PageInfo.DEFAULT)
        Log.i("FindPage", "$pageResult")
        return RewizorResult(pageResult.model.records)
    }

    override suspend fun getPublicationsList(page: Int, pageCount: Int, topic: String, category: String?): RewizorResult<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}