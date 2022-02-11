package app.khodko.catfood.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.khodko.catfood.data.OnlinerRepository
import app.khodko.catfood.data.ProductResult
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProductViewModel(val key: String) : ViewModel() {

    private val onlinerRepository = OnlinerRepository()

    private val _product = MutableLiveData<ProductResult>().apply { load() }
    val product: LiveData<ProductResult> = _product

    fun load() {
        viewModelScope.launch {
            val value = try {
                ProductResult.Success(onlinerRepository.getProductAsync(key))
            } catch (e: IOException) {
                ProductResult.Error(e)
            } catch (e: HttpException) {
                ProductResult.Error(e)
            }
            _product.postValue(value)
        }
    }

}