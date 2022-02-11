package app.khodko.catfood.api.onliner

enum class BrandType(val type: String, val viewName: String) {

    EVERYTHING("", "Everything"),
    PROPLAN("proplan", "Pro Plan");

    override fun toString(): String {
        return viewName
    }
}