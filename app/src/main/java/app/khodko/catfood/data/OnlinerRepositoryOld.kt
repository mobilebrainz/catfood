package app.khodko.catfood.data

import androidx.annotation.WorkerThread
import app.khodko.catfood.api.onliner.CatFoodType
import app.khodko.catfood.api.onliner.OnlinerRequest

class OnlinerRepositoryOld {

    @WorkerThread
    suspend fun getProductsAsync(typefood: CatFoodType, page: Int) =
        OnlinerRequest().getProductsAsync(typefood, page)


    @WorkerThread
    suspend fun getProductAsync(key: String) =
        OnlinerRequest().getProductAsync(key)
}