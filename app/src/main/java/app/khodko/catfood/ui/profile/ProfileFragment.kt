package app.khodko.catfood.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.khodko.catfood.core.extension.getActivityViewModelExt
import app.khodko.catfood.core.extension.getViewModelExt
import app.khodko.catfood.core.extension.navigateExt
import app.khodko.catfood.core.extension.showExt
import app.khodko.catfood.databinding.FragmentProfileBinding
import app.khodko.catfood.ui.activity.MainViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileViewModel: ProfileViewModel
    var account: GoogleSignInAccount? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel = getViewModelExt { ProfileViewModel() }
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        account = getActivityViewModelExt { MainViewModel() }.account

        account?.let {
            show(it)
            binding.btnFavorites.setOnClickListener {
                navigateExt(ProfileFragmentDirections.actionNavProfileToNavFavorites())
            }
        }

        return binding.root
    }

    private fun show(account: GoogleSignInAccount) {
        with(account) {
            binding.nameView.text = displayName
            //familyName
            binding.emailView.text = email
            binding.idView.text = id
            //val photo = photoUrl.toString()
            val photo = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9c/Golden_star.svg/512px-Golden_star.svg.png"
            binding.imageView.showExt(photo)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}