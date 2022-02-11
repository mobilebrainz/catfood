package app.khodko.catfood.ui.products

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.khodko.catfood.R
import app.khodko.catfood.api.onliner.CatFoodType
import app.khodko.catfood.api.onliner.KeyType
import app.khodko.catfood.api.onliner.Query
import app.khodko.catfood.core.BaseFragment
import app.khodko.catfood.core.extension.getViewModelExt
import app.khodko.catfood.core.extension.navigateExt
import app.khodko.catfood.data.SearchResult
import app.khodko.catfood.databinding.FragmentHomeBinding
import com.google.android.material.chip.ChipGroup


class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagedProductsViewModel: PagedProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val defaultQuery = Query(KeyType.CATFOOD)
        pagedProductsViewModel = getViewModelExt { PagedProductsViewModel(defaultQuery) }
        addDividers()
        bindState()
        binding.swipeRefreshLayout.isEnabled = false
        return binding.root
    }

    private fun bindState() {
        val adapter = ProductsAdapter()
        binding.recycler.adapter = adapter

        bindFilter()
        bindList(adapter)
    }

    private fun bindFilter() {
        binding.typeFoodChipGroup.setOnCheckedChangeListener { _: ChipGroup?, checkedId: Int ->
            val foodType = when(checkedId) {
                R.id.dryfoodChip -> CatFoodType.DRYFOOD
                R.id.cannedChip -> CatFoodType.CANNED
                R.id.preservsChip -> CatFoodType.PRESERVS
                R.id.tastyChip -> CatFoodType.TASTY
                else -> null
            }
            updateListFromInput(foodType, pagedProductsViewModel.accept)
        }
    }

    private fun updateListFromInput(typefood: CatFoodType?, onQueryChanged: (UiAction.Search) -> Unit) {
        val stateTypeFood = pagedProductsViewModel.state.value?.query?.typefood
        if (typefood != stateTypeFood) {
            binding.recycler.scrollToPosition(0)
            val query = Query(KeyType.CATFOOD)
            query.typefood = typefood
            onQueryChanged(UiAction.Search(query))
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
        binding.recycler.isVisible = !show
    }

    private fun setupScrollListener(onScrollChanged: (UiAction.Scroll) -> Unit) {
        val layoutManager = binding.recycler.layoutManager as LinearLayoutManager
        binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        binding.recycler.addItemDecoration(decoration)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //menu.clear()
        inflater.inflate(R.menu.home_fragment_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.filter -> {
                navigateExt(HomeFragmentDirections.actionNavHomeToNavFilter())
                true
            }
            else -> false
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}