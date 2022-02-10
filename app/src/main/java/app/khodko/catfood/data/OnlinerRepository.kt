package app.khodko.catfood.data

import app.khodko.catfood.api.onliner.OnlinerService

class OnlinerRepository {

    private val service = OnlinerService.create()

    suspend fun getProductAsync(key: String) =
        service.getProduct(key)
}