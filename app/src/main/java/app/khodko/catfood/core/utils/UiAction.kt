package app.khodko.catfood.core.utils

sealed class UiAction {
    data class Search(val query: String) : UiAction()
    data class Scroll(
        val visibleItemCount: Int,
        val lastVisibleItemPosition: Int,
        val totalItemCount: Int
    ) : UiAction()
}