package app.khodko.catfood.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.khodko.catfood.data.OnlinerRepository
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val onlinerRepository = OnlinerRepository()

    fun loadProduct(key: String) {
        viewModelScope.launch {
            val response = onlinerRepository.getProductAsync(key)

        }
    }

}