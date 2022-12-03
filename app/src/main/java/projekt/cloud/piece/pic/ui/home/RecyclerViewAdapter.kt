package projekt.cloud.piece.pic.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import projekt.cloud.piece.pic.R
import projekt.cloud.piece.pic.api.ApiCategories.CategoriesResponseBody.Data.Category
import projekt.cloud.piece.pic.databinding.LayoutRecyclerHomeBinding
import projekt.cloud.piece.pic.ui.home.RecyclerViewAdapter.RecyclerViewHolder
import projekt.cloud.piece.pic.util.CoroutineUtil.io
import projekt.cloud.piece.pic.util.CoroutineUtil.ui

class RecyclerViewAdapter(private val categories: List<Category>,
                          private val thumbs: MutableMap<String, Bitmap?>,
                          private val onClick: (Category, View) -> Unit): RecyclerView.Adapter<RecyclerViewHolder>() {

    inner class RecyclerViewHolder(private val binding: LayoutRecyclerHomeBinding):
        RecyclerView.ViewHolder(binding.root), OnClickListener {
        constructor(parent: ViewGroup): this(parent.context)
        constructor(context: Context): this(LayoutInflater.from(context))
        constructor(layoutInflater: LayoutInflater): this(LayoutRecyclerHomeBinding.inflate(layoutInflater))

        private var job: Job? = null

        fun setCategory(category: Category, thumbs: MutableMap<String, Bitmap?>) {
            binding.category = category
            binding.root.transitionName = binding.root.resources.getString(R.string.list_transition_prefix) + category.title
            binding.root.setOnClickListener(this)
            // job?.cancel()
            job = ui {
                if (!thumbs.containsKey(category.title)) {
                    val bitmap = withContext(io) {
                        category.thumb.bitmap
                    }
                    thumbs[category.title] = bitmap
                }
                binding.bitmap = thumbs[category.title]
                job = null
            }
        }

        override fun onClick(v: View) {
            binding.category?.let { onClick.invoke(it, v) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setCategory(categories[position], thumbs)
    }

    override fun getItemCount() = categories.size

}