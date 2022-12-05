package projekt.cloud.piece.pic.ui.read

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialSharedAxis
import projekt.cloud.piece.pic.R
import projekt.cloud.piece.pic.base.BaseFragment
import projekt.cloud.piece.pic.databinding.FragmentReadBinding
import projekt.cloud.piece.pic.ui.read.content.ComicContentFragment

class ReadFragment: BaseFragment() {
    
    private companion object {
        const val ARG_INDEX = "index"
    }
    
    private var _binding: FragmentReadBinding? = null
    private val binding: FragmentReadBinding
        get() = _binding!!
    private val root get() = binding.root
    
    private val readComic: ReadComic by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }
    
    private var currentContentFragment: Fragment? = null
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReadBinding.inflate(inflater, container, false)
        
        val arguments = requireArguments()
        var transitionName = getString(R.string.read_transition)
        transitionName = arguments.getString(transitionName, transitionName)
        binding.root.transitionName = transitionName
        
        readComic.index = arguments.getInt(ARG_INDEX)
        
        return root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateContentFragment(requireAnimation = false)
        readComic.prev.observe(viewLifecycleOwner) {
            updateContentFragment()
            updateRootTransitionName(it)
        }
        readComic.next.observe(viewLifecycleOwner) {
            updateContentFragment(isForward =  false)
            updateRootTransitionName(it)
        }
    }
    
    private fun updateContentFragment(fragment: Fragment = ComicContentFragment(),
                                      requireAnimation: Boolean = true,
                                      isForward: Boolean = true) {
        if (requireAnimation) {
            currentContentFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, isForward)
            fragment.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, isForward)
        }
        childFragmentManager.commit {
            replace(R.id.fragment_container_view, fragment)
        }
        currentContentFragment = fragment
    }
    
    private fun updateRootTransitionName(index: Int) {
        root.transitionName = getString(R.string.read_transition_prefix) + index
    }
    
}