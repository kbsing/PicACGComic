package projekt.cloud.piece.pic.ui.comicDetail

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.MenuProvider
import androidx.core.view.get
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.transition.platform.MaterialContainerTransform
import kotlinx.coroutines.withContext
import projekt.cloud.piece.pic.ComicCover
import projekt.cloud.piece.pic.R
import projekt.cloud.piece.pic.api.ApiComics.ComicResponseBody
import projekt.cloud.piece.pic.api.ApiComics.comic
import projekt.cloud.piece.pic.api.CommonBody.bitmap
import projekt.cloud.piece.pic.base.BaseFragment
import projekt.cloud.piece.pic.databinding.FragmentComicDetailBinding
import projekt.cloud.piece.pic.util.CoroutineUtil.io
import projekt.cloud.piece.pic.util.CoroutineUtil.ui
import projekt.cloud.piece.pic.util.DisplayUtil.deviceBounds
import projekt.cloud.piece.pic.util.FragmentUtil.setSupportActionBar
import projekt.cloud.piece.pic.util.HttpUtil.RESPONSE_CODE_SUCCESS
import projekt.cloud.piece.pic.util.RequestFailedMethodBlock
import projekt.cloud.piece.pic.util.RequestSuccessMethodBlock
import projekt.cloud.piece.pic.util.ResponseUtil.decodeJson

class ComicDetailFragment: BaseFragment(), OnClickListener {

    companion object {
        private const val ARG_ID = "id"
    }
    
    class Comic: ViewModel() {
        
        private val _comic = MutableLiveData<ComicResponseBody.Data.Comic?>()
        val comic: LiveData<ComicResponseBody.Data.Comic?>
            get() = _comic
        
        private val _avatar = MutableLiveData<Bitmap?>()
        val avatar: LiveData<Bitmap?>
            get() = _avatar
        
        var id: String? = null
        
        fun requestComicInfo(token: String?,
                             success: RequestSuccessMethodBlock,
                             failed: RequestFailedMethodBlock) {
            
            token ?: return failed.invoke(R.string.comic_detail_snack_not_logged)
            val id = id ?: return failed.invoke(R.string.comic_detail_snack_arg_required)
            
            viewModelScope.ui {
                val response = withContext(io) {
                    comic(id, token)
                } ?: return@ui failed.invoke(R.string.comic_detail_exception)
                
                if (response.code != RESPONSE_CODE_SUCCESS) {
                    return@ui failed.invoke(R.string.comic_detail_error_code)
                }
                
                val comic = response.decodeJson<ComicResponseBody>().data.comic
                _comic.value = comic
                
                _avatar.value = withContext(io) {
                    comic.creator.avatar.bitmap
                }
            }
        }
        
    }

    private var _binding: FragmentComicDetailBinding? = null
    private val binding: FragmentComicDetailBinding
        get() = _binding!!
    private val root get() = binding.root
    private val collapsingToolbarLayout: CollapsingToolbarLayout
        get() = binding.collapsingToolbarLayout
    private val toolbar: MaterialToolbar
        get() = binding.materialToolbar
    private val bottomAppBar: BottomAppBar
        get() = binding.bottomAppBar
    private val floatingActionButton: FloatingActionButton
        get() = binding.floatingActionButton
    private val tagGroup: ChipGroup
        get() = binding.chipGroup
    private val creator: MaterialCardView
        get() = binding.materialCardView
    private val creatorDetailIndicator: MaterialCheckBox
        get() = binding.materialCheckBox
    private val creatorDetail
        get() = binding.linearLayoutCompat
    private val nestedScrollView: NestedScrollView
        get() = binding.nestedScrollView

    private var id: String? = null

    private val comic: Comic by viewModels()
    private val comicCover: ComicCover by viewModels(
        ownerProducer = { requireActivity() }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
        val arguments = requireArguments()
        if (arguments.containsKey(ARG_ID)) {
            comic.id = requireArguments().getString(ARG_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentComicDetailBinding.inflate(inflater, container, false)
        binding.root.transitionName = requireArguments().getString(getString(R.string.comic_detail_transition))
        collapsingToolbarLayout.updateLayoutParams<AppBarLayout.LayoutParams> {
            height = requireContext().deviceBounds.width() * 4 / 3
        }
        binding.comic = comic
        binding.comicCover = comicCover
        binding.lifecycleOwner = viewLifecycleOwner
        
        applicationConfigs.token.observe(viewLifecycleOwner) {
            comic.requestComicInfo(
                it,
                success = {  },
                failed = {  }
            )
        }
        
        val fabMarginBottom = floatingActionButton.marginBottom
        applicationConfigs.windowInsetBottom.observe(viewLifecycleOwner) {
            floatingActionButton.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                updateMargins(bottom = fabMarginBottom + it)
            }
        }
        floatingActionButton.setOnClickListener(this)
        
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        setSupportActionBar(bottomAppBar)
        bottomAppBar.performHide(false)
        toolbar.setupWithNavController(navController)
        nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX, scrollY, _, _ ->
            when {
                scrollY >= v[0].measuredHeight - v.measuredHeight -> {
                    if (!bottomAppBar.isScrolledDown) {
                        bottomAppBar.performHide()
                    }
                }
                else -> {
                    if (!bottomAppBar.isScrolledUp) {
                        bottomAppBar.performShow()
                    }
                }
            }
        }
        comic.comic.observe(viewLifecycleOwner) {
            it?.tags?.let { tags -> addTags(tags) }
        }
        creator.setOnClickListener(this)
        requireActivity().addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_comic_detail, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_likes -> {}
                    R.id.menu_star -> {}
                }
                return true
            }
        }, viewLifecycleOwner, State.CREATED)
    }

    private fun addTags(tags: List<String>) {
        tags.forEach { tag ->
            tagGroup.addView(
                Chip(requireContext()).apply {
                    text = tag
                    isCloseIconVisible = false
                    setOnClickListener {}
                }
            )
        }
    }
    
    override fun onDestroyView() {
        _binding = null
        comicCover.setCover(null)
        super.onDestroyView()
    }
    
    override fun onClick(v: View?) {
        when (v) {
            creator -> {
                creatorDetail.visibility = when (creatorDetail.visibility) {
                    VISIBLE -> GONE
                    else -> VISIBLE
                }
                creatorDetailIndicator.isChecked = creatorDetail.visibility == VISIBLE
            }
            floatingActionButton -> {}
        }
    }
    
}