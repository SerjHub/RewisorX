package com.app.rewizor.data.repository

import com.app.rewizor.data.RewizorResult
import com.app.rewizor.data.model.Region
import com.app.rewizor.data.model.RewizorCategory

interface SystemRepository {
    var rewizorCategories: List<RewizorCategory>
    var regions: MutableList<Region>
    suspend fun coldStart(): RewizorResult<Boolean>
}