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
import app.khodko.catfood.data.SearchResult
import app.khodko.catfood.databinding.FragmentHomeBinding
import app.khodko.catfood.ui.search.SearchAdapter
import app.khodko.catfood.ui.search.SearchViewModel
import app.khodko.catfood.ui.home.UiAction
import app.khodko.catfood.ui.home.UiState

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var catfoodViewModel: CatfoodViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        catfoodViewModel = getViewModelExt { CatfoodViewModel() }
        addDividers()
        bindState()
        binding.swipeRefreshLayout.isEnabled = false
        return binding.root
    }

    private fun bindState() {
        val adapter = SearchAdapter()
        binding.recycler.adapter = adapter

        //bindSearch()
        bindList(adapter)
    }

    // todo: change on filter
    /*
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
                binding.recycler.scrollToPosition(0)
                onQueryChanged(UiAction.Search(query = it.toString()))
            }
        }
    }
    */

    private fun bindList(adapter: SearchAdapter) {
        setupScrollListener(catfoodViewModel.accept)

        catfoodViewModel.state.map(UiState::searchResult).distinctUntilChanged()
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is SearchResult.Success -> {
                        showEmptyList(result.data.isEmpty())
                        adapter.submitList(result.data)
                    }
                    is SearchResult.Error -> {
                        // todo: snakebar
                        Toast.makeText(
                            requireContext(), "\uD83D\uDE28 Wooops $result.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }

    private fun showEmptyList(show: Boolean) {
        //binding.emptyList.isVisible = show
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

    // add dividers between RecyclerView's row items
    private fun addDividers() {
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.recycler.addItemDecoration(decoration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}