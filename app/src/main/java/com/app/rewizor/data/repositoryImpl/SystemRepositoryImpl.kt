package com.app.rewizor.data.repositoryImpl

import com.app.rewizor.data.RewizorError
import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.Region
import com.app.rewizor.data.model.RewizorCategory
import com.app.rewizor.data.repository.AccountRepository
import com.app.rewizor.data.repository.SystemRepository
import com.app.rewizor.preferences.PreferencesCache
import com.app.rewizor.remote.RestClient

class SystemRepositoryImpl(
    private val prefs: PreferencesCache,
    private val accountRepository: AccountRepository,
    private val apiClient: RestClient
) :
    SystemRepository {

    init {

    }

    override lateinit var rewizorCategories: List<RewizorCategory>
    override lateinit var regions: MutableList<Region>

    var isInitialized: Boolean = false

    override suspend fun coldStart(): RewizorResult<Boolean> {
        if (isInitialized) return RewizorResult(true)
        isInitialized = true
        //init api
        apiClient.setEndpoint()

        val categoriesResult = apiClient.run { callApi { api.getCategories() } }
        if (categoriesResult.isError) return RewizorResult(
            false,
            RewizorError(message = "Ошибка получения категорий публикаций")
        )
        rewizorCategories = listOf(RewizorCategory.ALL).plus(categoriesResult.map(listOf()).model as MutableList)


        val regionsResult = apiClient.run { callApi { api.getRegions() } }
        if (regionsResult.isError) return RewizorResult(false, RewizorError(message = "Ошибка получения городов"))
        regions = mutableListOf()
        regionsResult.map(listOf()).model.forEach {
            regions.add(
                Region(
                    it.id,
                    it.name,
                    regions.none { reg -> reg.name.first() == it.name.first() }
                )
            )
        }
        accountRepository.systemRegions = regions

        //если аккаунт не анонимный - запрашиваем на старте, если анонимный - подставляем сохраненный регион
        if (AccountRepositoryImpl.ANON_TOKEN != prefs.sessionToken && prefs.sessionToken != null) {
            val accountResult = accountRepository.getAccount()
            if (accountResult.isError) {
                return RewizorResult(false, RewizorError(message = "Ошибка получения аккаунта"))
            }
        } else {
            setCity()
        }

        return RewizorResult(true)


//        if (prefs.sessionToken != null) {
//            val accountResult = accountRepository.getAccount()
//            if (accountResult.isError) {
//                apiClient.accessToken = AccountRepositoryImpl.ANON_TOKEN
//                accountRepository.getAccount()
//              //  return RewizorResult(false, RewizorError(message = "Ошибка получения аккаунта"))
//            }
//        }

    }

    private fun setCity() {
        with(accountRepository) {
            account = account.copy(
                region = regions.find { region ->
                    region.id == prefs.savedRegionId.let {
                        if (it == 0) regions.first().id else it
                    }
                }
            )
        }
    }
}