package com.gdsc.sellr.data.repository

import com.gdsc.sellr.model.Sell
import com.gdsc.sellr.util.Resource
import dagger.Module
import dagger.Provides


interface MainRepo {
    suspend fun getAllSells(
        result: (Resource<List<Sell>>) -> Unit
    )
}