package projekt.cloud.piece.pic.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import projekt.cloud.piece.pic.api.ApiCategories.CategoriesResponseBody.Data.Category
import projekt.cloud.piece.pic.api.ApiCategories.thumb
import projekt.cloud.piece.pic.databinding.LayoutRecyclerHomeBinding
import projekt.cloud.piece.pic.ui.home.RecyclerViewAdapter.RecyclerViewHolder
import projekt.cloud.piece.pic.util.CoroutineUtil.io
import projekt.cloud.piece.pic.util.CoroutineUtil.ui

class RecyclerViewAdapter(private val onClick: (Category) -> Unit): RecyclerView.Adapter<RecyclerViewHolder>() {

    inner class RecyclerViewHolder(private val binding: LayoutRecyclerHomeBinding):
        RecyclerView.ViewHolder(binding.root), OnClickListener {
        constructor(parent: ViewGroup): this(parent.context)
        constructor(context: Context): this(LayoutInflater.from(context))
        constructor(layoutInflater: LayoutInflater): this(LayoutRecyclerHomeBinding.inflate(layoutInflater))

        private var job: Job? = null

        fun setCategory(category: Category, thumbs: MutableMap<String, Bitmap>) {
            binding.category = category
            binding.root.setOnClickListener(this)
            job?.cancel()
            job = ui {
                Log.e("setCategory", category.title)
                var bitmap = thumbs[category.title]
                if (bitmap == null) {
                    bitmap = withContext(io) {
                        thumb(category)?.body?.byteStream()?.let {
                            BitmapFactory.decodeStream(it)
                        }
                    }
                    bitmap?.let { thumbs[category.title] = it }
                }
                binding.bitmap = bitmap
                job = null
            }
        }

        override fun onClick(v: View?) {
            binding.category?.let { onClick.invoke(it) }
        }

    }

    var categories: List<Category>? = null
        set(value) {
            thumbs.clear()
            field = value
            @Suppress("NotifyDataSetChanged")
            notifyDataSetChanged()
        }

    private var thumbs = mutableMapOf<String, Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        categories?.get(position)?.let {
            holder.setCategory(it, thumbs)
        }
    }

    override fun getItemCount() = categories?.size ?: 0

}