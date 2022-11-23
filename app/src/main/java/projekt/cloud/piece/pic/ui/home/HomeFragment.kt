package projekt.cloud.piece.pic.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.State
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.transition.platform.Hold
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import projekt.cloud.piece.pic.ApplicationConfigs
import projekt.cloud.piece.pic.R
import projekt.cloud.piece.pic.api.ApiCategories.CategoriesResponseBody
import projekt.cloud.piece.pic.api.ApiCategories.categories
import projekt.cloud.piece.pic.databinding.FragmentHomeBinding
import projekt.cloud.piece.pic.util.CoroutineUtil.io
import projekt.cloud.piece.pic.util.CoroutineUtil.ui

class HomeFragment: Fragment(), OnClickListener {

    companion object {
        private const val RECYCLER_VIEW_MAX_SPAN = 2
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private val root get() = binding.root
    private val bottomAppBar: BottomAppBar
        get() = binding.bottomAppBar
    private val search: MaterialCardView
        get() = binding.materialCardViewSearch
    private val floatingActionButton: FloatingActionButton
        get() = binding.floatingActionButton
    private val recyclerView: RecyclerView
        get() = binding.recyclerView

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

        val floatingActionButtonMarginBottom = floatingActionButton.marginBottom
        applicationConfigs.windowInsetBottom.observe(viewLifecycleOwner) {
            floatingActionButton.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                updateMargins(bottom = floatingActionButtonMarginBottom + it)
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).setSupportActionBar(bottomAppBar)
        search.setOnClickListener(this)
        floatingActionButton.setOnClickListener(this)

        val recyclerViewAdapter = RecyclerViewAdapter {

        }
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), RECYCLER_VIEW_MAX_SPAN)

        val spacingOuterHor = resources.getDimensionPixelSize(R.dimen.md_spec_spacing_hor_8)
        val spacingInnerHor = resources.getDimensionPixelSize(R.dimen.md_spec_spacing_hor_4)
        val spacingInnerVer = resources.getDimensionPixelSize(R.dimen.md_spec_spacing_ver_8)
        recyclerView.addItemDecoration(
            object: RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
                    super.getItemOffsets(outRect, view, parent, state)

                    val pos = parent.getChildLayoutPosition(view)
                    when (pos % RECYCLER_VIEW_MAX_SPAN) {
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
                    val nonFullRow = itemCount % RECYCLER_VIEW_MAX_SPAN
                    when (pos) {
                        in itemCount - (if (nonFullRow != 0) nonFullRow else RECYCLER_VIEW_MAX_SPAN) until itemCount ->
                            applicationConfigs.windowInsetBottom.value?.let { outRect.bottom = it }
                        else -> outRect.bottom = spacingInnerVer
                    }
                }
            }
        )

        applicationConfigs.token.observe(viewLifecycleOwner) { token ->
            token?.let {
                ui {
                    recyclerViewAdapter.categories = withContext(io) {
                        categories(token)?.body
                            ?.string()
                            ?.let { json -> Json.decodeFromString<CategoriesResponseBody>(json) }
                            ?.data
                            ?.categories
                            ?.filter { !it.isWeb }
                    }
                }
            }
        }
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
            floatingActionButton -> navController.navigate(
                HomeFragmentDirections.actionHomeToAccount(),
                FragmentNavigatorExtras(floatingActionButton to floatingActionButton.transitionName)
            )
        }
    }

}