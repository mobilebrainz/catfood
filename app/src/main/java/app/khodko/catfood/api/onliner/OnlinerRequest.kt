package app.khodko.catfood.api.onliner

import app.khodko.catfood.api.WebService


class OnlinerRequest : OnlinerRequestInterface {

    companion object {
        const val ONLINER_WEB_SERVICE = "https://catalog.api.onliner.by/"
        const val ONLINER_SEARCH_CATFOOD = ONLINER_WEB_SERVICE + "search/catfood"
        const val ONLINER_PRODUCT = ONLINER_WEB_SERVICE + "products/"
    }

    private fun createJsonRetrofitService() = WebService
        .createJsonRetrofitService(
            OnlinerRequestService::class.java,
            ONLINER_WEB_SERVICE
        )

    override suspend fun getProductsAsync(typefood: CatFoodType, page: Int) =
        createJsonRetrofitService().getProductsAsync(typefood.getType(), page)

    override suspend fun getProductAsync(key: String) =
        createJsonRetrofitService().getProductAsync(key)
}