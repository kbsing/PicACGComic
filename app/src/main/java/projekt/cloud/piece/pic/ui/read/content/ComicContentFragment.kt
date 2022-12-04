package projekt.cloud.piece.pic.ui.read.content

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import projekt.cloud.piece.pic.Comic
import projekt.cloud.piece.pic.api.ApiComics.EpisodeContentResponseBody.Data.Pages
import projekt.cloud.piece.pic.api.ApiComics.EpisodeContentResponseBody.Data.Pages.Doc
import projekt.cloud.piece.pic.base.BaseFragment
import projekt.cloud.piece.pic.databinding.FragmentComicContentBinding
import projekt.cloud.piece.pic.ui.read.ReadComic
import projekt.cloud.piece.pic.ui.read.ReadFragment

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
    
    private var job: Job? = null
    
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
        val recyclerViewAdapter = RecyclerViewAdapter(docs, images)
        recyclerView.adapter = recyclerViewAdapter
    }

}