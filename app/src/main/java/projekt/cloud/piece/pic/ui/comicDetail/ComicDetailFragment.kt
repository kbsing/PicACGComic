package projekt.cloud.piece.pic.ui.comicDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.transition.platform.MaterialContainerTransform
import projekt.cloud.piece.pic.R
import projekt.cloud.piece.pic.base.BaseFragment
import projekt.cloud.piece.pic.databinding.FragmentComicDetailBinding

class ComicDetailFragment: BaseFragment() {

    companion object {
        private const val ARG_ID = "id"
    }

    private var _binding: FragmentComicDetailBinding? = null
    private val binding: FragmentComicDetailBinding
        get() = _binding!!
    private val root get() = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentComicDetailBinding.inflate(inflater, container, false)
        binding.root.transitionName = requireArguments().getString(getString(R.string.comic_detail_transition))
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

}