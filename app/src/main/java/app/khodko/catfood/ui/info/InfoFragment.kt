package app.khodko.catfood.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.khodko.catfood.core.extension.getViewModelExt
import app.khodko.catfood.databinding.FragmentInfoBinding
import app.khodko.catfood.R

class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var infoViewModel: InfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        infoViewModel = getViewModelExt{ InfoViewModel() }
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        initPages()
        return binding.root
    }

    private fun initPages() {
        val pages: Array<String> = resources.getStringArray(R.array.help_pages_array)
        val adapter = InfoPagerAdapter(pages)
        binding.viewPager.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}