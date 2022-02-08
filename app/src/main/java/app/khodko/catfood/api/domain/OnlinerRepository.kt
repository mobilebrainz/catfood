package app.khodko.catfood.api.domain

import androidx.annotation.WorkerThread
import app.khodko.catfood.api.onliner.CatFoodType
import app.khodko.catfood.api.onliner.OnlinerRequest

class OnlinerRepository {

    @WorkerThread
    suspend fun getProductsAsync(typefood: CatFoodType, page: Int) =
        OnlinerRequest().getProductsAsync(typefood, page)


    @WorkerThread
    suspend fun getProductAsync(key: String) =
        OnlinerRequest().getProductAsync(key)
}