package projekt.cloud.piece.pic.ui.read.content

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.withContext
import okhttp3.Response
import projekt.cloud.piece.pic.Comic
import projekt.cloud.piece.pic.R
import projekt.cloud.piece.pic.api.ApiComics
import projekt.cloud.piece.pic.api.ApiComics.EpisodeContentResponseBody.Data
import projekt.cloud.piece.pic.api.ApiComics.EpisodeContentResponseBody.Data.Pages
import projekt.cloud.piece.pic.api.ApiComics.EpisodeContentResponseBody.Data.Pages.Doc
import projekt.cloud.piece.pic.api.ApiComics.episodeContent
import projekt.cloud.piece.pic.base.BaseFragment
import projekt.cloud.piece.pic.databinding.FragmentComicContentBinding
import projekt.cloud.piece.pic.ui.read.ReadComic
import projekt.cloud.piece.pic.ui.read.ReadFragment
import projekt.cloud.piece.pic.util.CoroutineUtil.io
import projekt.cloud.piece.pic.util.CoroutineUtil.ui
import projekt.cloud.piece.pic.util.FragmentUtil.setSupportActionBar
import projekt.cloud.piece.pic.util.HttpUtil.RESPONSE_CODE_SUCCESS
import projekt.cloud.piece.pic.util.ResponseUtil.decodeJson

class ComicContentFragment: BaseFragment() {

    private val readFragment: ReadFragment
        get() = findParentAs()
    
    private val comic: Comic by activityViewModels()
    
    private val readComic: ReadComic by viewModels(
        ownerProducer = { readFragment }
    )
    
    private var _binding: FragmentComicContentBinding? = null
    private val binding: FragmentComicContentBinding
        get() = _binding!!
    private val root get() = binding.root
    private val toolbar: MaterialToolbar
        get() = binding.materialToolbar
    private val recyclerView: RecyclerView
        get() = binding.recyclerView
    
    private lateinit var navController: NavController
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentComicContentBinding.inflate(inflater, container, false)
        return root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController)
        
        val pages = arrayListOf<Pages>()
        val docs = arrayListOf<Doc>()
        val images = mutableMapOf<String, Bitmap?>()
        val recyclerViewAdapter = RecyclerViewAdapter(lifecycleScope, docs, images)
        recyclerView.adapter = recyclerViewAdapter
        
        lifecycleScope.ui {
            val token = applicationConfigs.token.value ?: return@ui failed(R.string.request_not_logged)
            val id = comic.id ?: return@ui failed(R.string.comic_content_snack_arg_required)
            
            toolbar.title = comic.docList[readComic.index].title
            toolbar.subtitle = comic.comic.value?.title
            
            var response: Response?
            var data: Data?
            while (true) {
                response = withContext(io) {
                    episodeContent(id, comic.docList[readComic.index].order, pages.size + 1, token = token)
                } ?: return@ui failed(R.string.comic_content_snack_exception)
                if (response.code != RESPONSE_CODE_SUCCESS) {
                    return@ui failed(R.string.comic_content_snack_error_code)
                }
                data = response.decodeJson<ApiComics.EpisodeContentResponseBody>().data
                
                pages.add(data.pages)
                docs.addAll(data.pages.docs)
    
                succeed()
                
                if (pages.size == data.pages.pages) {
                    break
                }
            }
        }
    }
    
    private fun succeed() {
        (recyclerView.adapter as RecyclerViewAdapter).notifyListUpdate()
        recyclerView.invalidate()
    }
    
    private fun failed(@StringRes message: Int) {
        sendMessage(message)
        navController.navigateUp()
    }

}