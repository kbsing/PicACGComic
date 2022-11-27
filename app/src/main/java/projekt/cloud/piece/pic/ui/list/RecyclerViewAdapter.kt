package projekt.cloud.piece.pic.ui.list

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import projekt.cloud.piece.pic.R
import projekt.cloud.piece.pic.api.ApiComics.ComicsResponseBody.Data.Comics.Doc
import projekt.cloud.piece.pic.api.CommonBody.bitmap
import projekt.cloud.piece.pic.databinding.LayoutRecyclerListBinding
import projekt.cloud.piece.pic.util.CoroutineUtil.io
import projekt.cloud.piece.pic.util.CoroutineUtil.ui

class RecyclerViewAdapter(private val onClick: (Doc) -> Unit):
    RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

    inner class RecyclerViewHolder(private val binding: LayoutRecyclerListBinding):
        RecyclerView.ViewHolder(binding.root), OnClickListener {
        
        constructor(view: View): this(view.context)
        constructor(context: Context): this(LayoutInflater.from(context))
        constructor(inflater: LayoutInflater): this(LayoutRecyclerListBinding.inflate(inflater))
        
        private var job: Job? = null
        
        init {
            binding.root.setOnClickListener(this)
        }
        
        fun setDoc(doc: Doc) {
            binding.doc = doc
            binding.bitmap = BitmapFactory.decodeResource(binding.root.resources, R.drawable.ic_round_image_24)
            job?.cancel()
            job = ui {
                binding.bitmap = withContext(io) {
                    doc.thumb.bitmap
                }
                job = null
            }
        }
    
        override fun onClick(v: View?) {
            binding.doc?.let(onClick)
        }
        
    }
    
    private var docSize = 0
    private val docs = arrayListOf<Doc>()

    fun addNewDocs(newDocs: List<Doc>) {
        docs.addAll(newDocs)
        notifyItemRangeInserted(docSize, newDocs.size)
        docSize += newDocs.size
    }

    fun clearAll() {
        notifyItemRangeRemoved(0, docSize)
        docs.clear()
        docSize = 0
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerViewHolder(parent)
    
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setDoc(docs[position])
    }
    
    override fun getItemCount() = docSize
    
}