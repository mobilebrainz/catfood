package app.khodko.catfood.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.khodko.catfood.core.extension.getViewModelExt
import app.khodko.catfood.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = getViewModelExt { HomeViewModel() }

        return binding.root
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