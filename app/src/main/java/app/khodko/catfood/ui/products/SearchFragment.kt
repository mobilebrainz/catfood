package app.khodko.catfood.ui.products

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.khodko.catfood.R
import app.khodko.catfood.api.onliner.KeyType
import app.khodko.catfood.api.onliner.Query
import app.khodko.catfood.core.BaseFragment
import app.khodko.catfood.core.extension.getViewModelExt
import app.khodko.catfood.core.extension.hideSoftKeyboardExt
import app.khodko.catfood.core.extension.navigateExt
import app.khodko.catfood.data.SearchResult
import app.khodko.catfood.databinding.FragmentSearchBinding

private const val DEFAULT_QUERY = "Royal Canin"

class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagedProductsViewModel: PagedProductsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val defaultQuery = Query(KeyType.PRODUCTS)
        defaultQuery.searchQuery = DEFAULT_QUERY
        pagedProductsViewModel = getViewModelExt { PagedProductsViewModel(defaultQuery) }
        addDividers()
        bindState()
        return binding.root
    }

    private fun bindState() {
        val adapter = ProductsAdapter()
        binding.list.adapter = adapter
        adapter.shotClickListener = { item, _ ->
            //if (!swipeRefreshLayout.isRefreshing)
            navigateExt(SearchFragmentDirections.actionNavSearchToNavProduct(item.key))
        }
        bindSearch()
        bindList(adapter)
    }

    private fun bindSearch() {
        binding.searchView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateListFromInput()
                true
            } else {
                false
            }
        }
        binding.searchView.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateListFromInput()
                true
            } else {
                false
            }
        }
        pagedProductsViewModel.state.map { it.query.searchQuery }.distinctUntilChanged()
            .observe(viewLifecycleOwner, binding.searchView::setText)
    }

    private fun updateListFromInput() {
        binding.searchView.text.trim().let {
            if (it.isNotEmpty()) {
                binding.list.scrollToPosition(0)
                hideSoftKeyboardExt()
                val query = Query(KeyType.PRODUCTS)
                query.searchQuery = it.toString()
                pagedProductsViewModel.accept(UiAction.Search(query))
            }
        }
    }

    private fun bindList(adapter: ProductsAdapter) {
        setupScrollListener(pagedProductsViewModel.accept)
        pagedProductsViewModel.state.map(UiState::searchResult).distinctUntilChanged()
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is SearchResult.Success -> {
                        showEmptyList(result.data.isEmpty())
                        adapter.submitList(result.data)
                    }
                    is SearchResult.Error -> {
                        showErrorSnackbar(R.string.error_app, R.string.snackbar_retry)
                            { pagedProductsViewModel.retry() }
                    }
                }
            }
    }

    private fun showEmptyList(show: Boolean) {
        binding.emptyList.isVisible = show
        binding.list.isVisible = !show
    }

    private fun setupScrollListener(onScrollChanged: (UiAction.Scroll) -> Unit) {
        val layoutManager = binding.list.layoutManager as LinearLayoutManager
        binding.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                onScrollChanged(
                    UiAction.Scroll(visibleItemCount, lastVisibleItem, totalItemCount)
                )
            }
        })
    }

    private fun addDividers() {
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}