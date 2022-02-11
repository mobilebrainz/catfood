package app.khodko.catfood.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.khodko.catfood.R
import app.khodko.catfood.core.BaseFragment
import app.khodko.catfood.core.extension.getViewModelExt
import app.khodko.catfood.data.ProductResult
import app.khodko.catfood.data.SearchResult
import app.khodko.catfood.databinding.FragmentProductBinding


class ProductFragment : BaseFragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        arguments?.let {
            val args = ProductFragmentArgs.fromBundle(it)
            val key = args.key
            productViewModel = getViewModelExt { ProductViewModel(key) }
            initObservers()
        }
        return binding.root
    }

    private fun initObservers() {
        productViewModel.product.observe(viewLifecycleOwner) {
            when (it) {
                is ProductResult.Success -> {
                    showErrorSnackbar(R.string.error_app, R.string.snackbar_retry)
                    {  }
                }
                is ProductResult.Error -> {
                    showErrorSnackbar(R.string.error_app, R.string.snackbar_retry)
                    { productViewModel.load() }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}