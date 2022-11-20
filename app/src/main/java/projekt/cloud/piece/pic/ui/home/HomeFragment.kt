package projekt.cloud.piece.pic.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.platform.Hold
import projekt.cloud.piece.pic.ApplicationConfigs
import projekt.cloud.piece.pic.databinding.FragmentHomeBinding

class HomeFragment: Fragment(), OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private val root get() = binding.root
    private val search: MaterialCardView
        get() = binding.materialCardViewSearch

    private lateinit var navController: NavController

    private val applicationConfigs: ApplicationConfigs by viewModels(ownerProducer = { requireActivity() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
        navController = findNavController()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.applicationConfigs = applicationConfigs
        binding.lifecycleOwner = viewLifecycleOwner
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        search.setOnClickListener(this)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        when (v) {
            search -> navController.navigate(
                HomeFragmentDirections.actionHomeToSearch(),
                FragmentNavigatorExtras(search to search.transitionName)
            )
        }
    }

}