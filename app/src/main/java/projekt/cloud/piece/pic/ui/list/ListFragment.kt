package projekt.cloud.piece.pic.ui.list

import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.transition.platform.MaterialContainerTransform
import kotlinx.coroutines.Job
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import projekt.cloud.piece.pic.ComicCover
import projekt.cloud.piece.pic.R
import projekt.cloud.piece.pic.api.ApiComics.ComicsResponseBody
import projekt.cloud.piece.pic.api.ApiComics.ComicsResponseBody.Data.Comics.Doc
import projekt.cloud.piece.pic.api.ApiComics.comics
import projekt.cloud.piece.pic.api.CommonParam.SORT_NEW_TO_OLD
import projekt.cloud.piece.pic.base.BaseFragment
import projekt.cloud.piece.pic.databinding.FragmentListBinding
import projekt.cloud.piece.pic.util.CoroutineUtil.io
import projekt.cloud.piece.pic.util.CoroutineUtil.ui
import projekt.cloud.piece.pic.util.FragmentUtil.setSupportActionBar

class ListFragment: BaseFragment() {

    companion object {
        private const val ARG_DEFAULT_VALUE = ""

        private const val ARG_CATEGORY = "category"
        private const val ARG_KEYWORD = "keyword"

        private const val GRID_SPAN = 2
    }

    class Comics: ViewModel() {

        var category: String? = null
        var keyword: String? = null

        val comicsResponseBodies = arrayListOf<ComicsResponseBody>()

        val docs = arrayListOf<Doc>()
        val covers = mutableMapOf<String, Bitmap?>()

    }
    
    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding
        get() = _binding!!
    private val root get() = binding.root
    private val toolbar: MaterialToolbar
        get() = binding.materialToolbar
    private val recyclerView: RecyclerView
        get() = binding.recyclerView

    private var sort = SORT_NEW_TO_OLD

    private var job: Job? = null

    private val comics: Comics by viewModels()
    private val comicCover: ComicCover by viewModels(
        ownerProducer = { requireActivity() }
    )

    private val comicsResponseBodies: ArrayList<ComicsResponseBody>
        get() = comics.comicsResponseBodies
    private val docs: ArrayList<Doc>
        get() = comics.docs
    private val covers: MutableMap<String, Bitmap?>
        get() = comics.covers

    private val category: String?
        get() = comics.category

    private var requireCaching = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
        val argument = requireArguments()
        comics.category = argument.getString(ARG_CATEGORY, ARG_DEFAULT_VALUE)
        comics.keyword = argument.getString(ARG_KEYWORD, ARG_DEFAULT_VALUE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater)
        binding.root.transitionName = requireArguments().getString(getString(R.string.list_transition))
        postponeEnterTransition()
        return root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        val navController = findNavController()
        toolbar.setupWithNavController(navController)
        val category = category
        when {
            !category.isNullOrBlank() -> toolbar.title = category
        }
        val recyclerViewAdapter = RecyclerViewAdapter(docs, covers) { doc, v ->
            requireCaching = true
            comicCover.setCover(covers[doc._id])
            navController.navigate(
                ListFragmentDirections.actionListToComicDetail(doc._id, v.transitionName),
                FragmentNavigatorExtras(v to v.transitionName)
            )
        }
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(GRID_SPAN, VERTICAL)
        recyclerView.doOnPreDraw { startPostponedEnterTransition() }

        val spacingOuterHor = resources.getDimensionPixelSize(R.dimen.md_spec_spacing_hor_16)
        val spacingInnerHor = resources.getDimensionPixelSize(R.dimen.md_spec_spacing_hor_4)
        val spacingOuterVer = resources.getDimensionPixelSize(R.dimen.md_spec_spacing_ver_8)
        var bottomInset = 0
        applicationConfigs.windowInsetBottom.value?.let {
            bottomInset = it
        }
        recyclerView.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
                super.getItemOffsets(outRect, view, parent, state)
                val pos = parent.getChildAdapterPosition(view)
                when ((view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex) {
                    0 -> {
                        outRect.left = spacingOuterHor
                        outRect.right = spacingInnerHor
                    }
                    else -> {
                        outRect.left = spacingInnerHor
                        outRect.right = spacingOuterHor
                    }
                }
                val itemCount = recyclerViewAdapter.itemCount
                outRect.bottom = when {
                    itemCount % GRID_SPAN == 0 && pos >= itemCount - GRID_SPAN -> bottomInset
                    pos == itemCount - 1 -> bottomInset
                    else -> spacingOuterVer
                }
            }
        })

        if (comicsResponseBodies.isEmpty()) {
            job = io {
                val response = startRequest()
                if (response != null) {
                    comicsResponseBodies.add(response)
                    docs.addAll(comicsResponseBodies.last().data.comics.docs)
                    ui { recyclerViewAdapter.notifyListUpdate() }
                }
                job = null
            }
        }
    }

    private fun startRequest(): ComicsResponseBody? {
        val category = category
        return when {
            !category.isNullOrBlank() -> requestComic(category, comicsResponseBodies.size + 1)
            else -> null
        }
    }

    private fun requestComic(category: String, page: Int) =
        applicationConfigs.token
            .value
            ?.let { comics(it, page, category, sort) }
            ?.body
            ?.string()
            ?.let { Json.decodeFromString<ComicsResponseBody>(it) }

    override fun onDestroyView() {
        if (!requireCaching) {
            docs.clear()
            covers.clear()
        }
        _binding = null
        super.onDestroyView()
    }
    
}