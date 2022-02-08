package app.khodko.catfood.api.onliner

enum class CatFoodType (val foodType: String) {
    DRYFOOD("dryfood"),
    CANNED("canned"),
    PRESERVS("preservs"),
    TASTY("tasty");

    fun getType() = foodType
}