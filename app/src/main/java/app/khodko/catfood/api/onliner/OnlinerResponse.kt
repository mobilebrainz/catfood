package app.khodko.catfood.api.onliner

import com.squareup.moshi.Json


data class SearchResponse(
    @field:Json(name = "products") val products: List<ProductResponse> = emptyList(),
    @field:Json(name = "total") val total: Int,
    @field:Json(name = "page") val page: PageResponse,
)

data class PageResponse(
    @field:Json(name = "limit") val limit: Int,
    @field:Json(name = "items") val items: Int,
    @field:Json(name = "current") val current: Int,
    @field:Json(name = "last") val last: Int,
)

data class ProductResponse(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "key") val key: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "full_name") val fullName: String,
    @field:Json(name = "name_prefix") val namePrifix: String,
    @field:Json(name = "extended_name") val extendedName: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "micro_description") val microDescription: String,
    @field:Json(name = "html_url") val htmlUrl: String,
    @field:Json(name = "url") val apiUrl: String,
    @field:Json(name = "prices") val prices: PricesResponse?,
    @field:Json(name = "images") val images: ImagesResponse?,
)

data class ImagesResponse(
    @field:Json(name = "header") val header: String?,
    //@field:Json(name = "icon") val icon: String?,
)

data class PricesResponse(
    @field:Json(name = "price_min") val priceMin: PriceResponse?,
    @field:Json(name = "price_max") val priceMax: PriceResponse?,
    @field:Json(name = "offers") val offers: OffersResponse?,
    @field:Json(name = "html_url") val htmlUrl: String?,
    @field:Json(name = "url") val apiUrl: String?,
)

data class PriceResponse(
    @field:Json(name = "amount") val amount: String,
    @field:Json(name = "currency") val currency: String,
)

data class OffersResponse(
    @field:Json(name = "count") val count: Int?,
)