package app.khodko.catfood.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.khodko.catfood.R
import app.khodko.catfood.api.onliner.ProductResponse
import app.khodko.catfood.core.BaseFragment
import app.khodko.catfood.core.extension.getViewModelExt
import app.khodko.catfood.core.extension.showExt
import app.khodko.catfood.data.ProductResult
import app.khodko.catfood.data.SearchResult
import app.khodko.catfood.databinding.FragmentProductBinding


class ProductFragment : BaseFragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel
    private lateinit var product: ProductResponse

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
        binding.swipeRefreshLayout.isEnabled = false
        return binding.root
    }

    private fun initObservers() {
        productViewModel.product.observe(viewLifecycleOwner) {
            when (it) {
                is ProductResult.Success -> {
                    product = it.data
                    with(product) {
                        binding.nameView.text = fullName
                        binding.descriptionView.text = description
                        showPrice(product)
                        showImage(product)
                        initListeners(product)
                    }
                }
                is ProductResult.Error -> {
                    showErrorSnackbar(R.string.error_app, R.string.snackbar_retry)
                    { productViewModel.load() }
                }
            }
        }
    }

    private fun showPrice(product: ProductResponse) {
        product.prices?.let {
            var priceStr = ""
            it.priceMin?.let { p -> priceStr += p.amount + " " + p.currency }
            it.priceMax?.let { p ->
                if (p.amount != it.priceMin?.amount) {
                    priceStr += " - " + p.amount + " " + p.currency
                }
            }
            if (priceStr.isNotEmpty()) {
                it.offers?.count?.let { c ->
                    val amountStr = getString(R.string.offers)
                    priceStr += " ($c $amountStr)"
                }
                binding.priceView.text = priceStr
            }
        }
    }

    private fun showImage(product: ProductResponse) {
        val imageUrl = product.images?.header
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            binding.imageView.showExt("https://$imageUrl")
        } else {
            binding.imageView.setImageResource(R.drawable.ic_image_24)
        }
    }

    private fun initListeners(product: ProductResponse) {
        binding.btnLink.setOnClickListener {
            val link = product.htmlUrl
            // todo: web-view
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}