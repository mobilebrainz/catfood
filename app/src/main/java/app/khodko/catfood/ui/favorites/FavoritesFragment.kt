package app.khodko.catfood.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.khodko.catfood.R
import app.khodko.catfood.core.extension.getActivityViewModelExt
import app.khodko.catfood.core.extension.getViewModelExt
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

        favoritesViewModel = getViewModelExt{ FavoritesViewModel() }
        userId = getActivityViewModelExt { MainViewModel() }.account?.id

        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}