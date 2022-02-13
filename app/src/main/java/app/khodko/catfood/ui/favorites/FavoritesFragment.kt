package app.khodko.catfood.ui.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import app.khodko.catfood.core.extension.getActivityViewModelExt
import app.khodko.catfood.core.extension.getViewModelExt
import app.khodko.catfood.core.extension.navigateExt
import app.khodko.catfood.databinding.FragmentFavoritesBinding
import app.khodko.catfood.ui.activity.MainViewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private var userId: String? = null
    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        userId = getActivityViewModelExt { MainViewModel() }.account?.id
        userId?.let { u ->
            favoritesViewModel = getViewModelExt { FavoritesViewModel(u) }
            initRecycler()
        }
        return binding.root
    }

    private fun initRecycler() {
        val recyclerView = binding.list
        val adapter = FavoritesAdapter()
        recyclerView.adapter = adapter
        addDividers()

        adapter.shotClickListener = { item, _ ->
            navigateExt(FavoritesFragmentDirections.actionNavFavoritesToNavProduct(item.productKey))
        }

        favoritesViewModel.favorites.observe(viewLifecycleOwner) {
            it.let { adapter.submitList(it) }
        }

        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.currentList[viewHolder.adapterPosition]
                favoritesViewModel.deleteFavorites(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
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