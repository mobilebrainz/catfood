package app.khodko.catfood.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.khodko.catfood.api.onliner.ProductResponse
import app.khodko.catfood.data.OnlinerRepository
import kotlinx.coroutines.launch

class ProductViewModel(val key: String) : ViewModel() {

    private val onlinerRepository = OnlinerRepository()

    private val _product = MutableLiveData<ProductResponse>().apply {
        viewModelScope.launch {
            // todo: exception?
            value = onlinerRepository.getProductAsync(key)
        }
    }
    val product: LiveData<ProductResponse> = _product

}