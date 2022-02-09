package app.khodko.catfood.ui.home

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.khodko.catfood.core.extension.getViewModelExt
import app.khodko.catfood.core.utils.UiAction
import app.khodko.catfood.databinding.FragmentHomeBinding
import app.khodko.catfood.model.SearchResult

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchViewModel: SearchViewModel


    //private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        searchViewModel = getViewModelExt { SearchViewModel() }
        addDividers()
        bindState()
        return binding.root
    }

    private fun bindState() {
        val adapter = OnlinerAdapter()
        binding.list.adapter = adapter

        bindSearch()
        bindList(adapter)
    }

    private fun bindSearch() {
        binding.searchRepo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput(searchViewModel.accept)
                true
            } else {
                false
            }
        }
        binding.searchRepo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput(searchViewModel.accept)
                true
            } else {
                false
            }
        }
        searchViewModel.state.map(UiState::query).distinctUntilChanged()
            .observe(viewLifecycleOwner, binding.searchRepo::setText)
    }

    private fun updateRepoListFromInput(onQueryChanged: (UiAction.Search) -> Unit) {
        binding.searchRepo.text.trim().let {
            if (it.isNotEmpty()) {
                binding.list.scrollToPosition(0)
                onQueryChanged(UiAction.Search(query = it.toString()))
            }
        }
    }

    private fun bindList(adapter: OnlinerAdapter) {
        setupScrollListener(searchViewModel.accept)

        searchViewModel.state.map(UiState::searchResult).distinctUntilChanged()
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is SearchResult.Success -> {
                        showEmptyList(result.data.isEmpty())
                        adapter.submitList(result.data)
                    }
                    is SearchResult.Error -> {
                        // todo: snakebar
                        Toast.makeText(
                            requireContext(),
                            "\uD83D\uDE28 Wooops $result.message}",
                            Toast.LENGTH_LONG
                        ).show()
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

    // add dividers between RecyclerView's row items
    private fun addDividers() {
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)
    }

    /*
    private fun load() {
        homeViewModel.loadProducts(CatFoodType.DRYFOOD, 1)
    }
     */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}