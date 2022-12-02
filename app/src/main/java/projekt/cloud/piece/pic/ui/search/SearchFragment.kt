package projekt.cloud.piece.pic.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.transition.platform.MaterialContainerTransform
import projekt.cloud.piece.pic.R
import projekt.cloud.piece.pic.base.BaseFragment
import projekt.cloud.piece.pic.databinding.FragmentSearchBinding

class SearchFragment: BaseFragment() {

    private lateinit var navController: NavController

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!
    private val root get() = binding.root
    private val toolbar: MaterialToolbar
        get() = binding.materialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.applicationConfigs = applicationConfigs
        binding.lifecycleOwner = viewLifecycleOwner
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener {
                navController.navigateUp()
            }
        }
        /**
         * ComponentActivity.addMenuProvider(MenuProvider, LifecycleOwner) might cause
         * showing of soft keyboard because of onDestroy() is called after
         * previous fragment's onResume()
         *
         * However, onStop() is called before both previous fragment's onResume() and
         * this fragment's onDestroy()
         **/
        requireActivity().addMenuProvider(
                object: MenuProvider {
                    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                        menu.clear()
                        menuInflater.inflate(R.menu.menu_search, menu)
                        (menu.findItem(R.id.action_search)?.actionView as? SearchView)?.let {
                            it.setIconifiedByDefault(false)
                            it.isIconified = false
                            it.removeIconAndFrame()
                            it.setOnQueryTextListener(object : OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    return true
                                }
                                override fun onQueryTextChange(newText: String?): Boolean {
                                    return true
                                }
                            })
                        }
                    }
                    override fun onMenuItemSelected(menuItem: MenuItem) = false
                },
                viewLifecycleOwner,
                Lifecycle.State.STARTED
        )
    }

    private fun SearchView.removeIconAndFrame() {
        this.javaClass.declaredFields.let { fields ->
            fields.find { it.name == "mSearchPlate" }?.let { field ->
                field.isAccessible = true
                (field.get(this) as? View)?.background = null
            }
            fields.find { it.name == "mCollapsedIcon" }?.let { field ->
                field.isAccessible = true
                (field.get(this) as? ImageView)?.let { imageView ->
                    imageView.setImageDrawable(null)
                    imageView.visibility = GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}