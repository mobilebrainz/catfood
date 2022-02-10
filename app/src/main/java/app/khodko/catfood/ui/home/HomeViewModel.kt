package app.khodko.catfood.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.khodko.catfood.data.OnlinerRepositoryOld
import app.khodko.catfood.api.onliner.CatFoodType
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val onlinerRepository = OnlinerRepositoryOld()

    fun loadProducts(typefood: CatFoodType, page: Int = 1) {
        viewModelScope.launch {
            val response = onlinerRepository.getProductsAsync(typefood, page)
            if (response.isSuccessful) {
                val searchResponse = response.body()
                val i = 0
            } else {
                val error = response.errorBody()
                val i = 0

                //code 422 - неверный параметр
            }
        }
    }

}