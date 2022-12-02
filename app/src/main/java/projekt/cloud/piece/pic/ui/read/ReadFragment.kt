package projekt.cloud.piece.pic.ui.read

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.transition.platform.MaterialContainerTransform
import projekt.cloud.piece.pic.R
import projekt.cloud.piece.pic.base.BaseFragment
import projekt.cloud.piece.pic.databinding.FragmentReadBinding

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
        readComic.prev.observe(viewLifecycleOwner) {
        }
        readComic.next.observe(viewLifecycleOwner) {
        }
    }
    
}