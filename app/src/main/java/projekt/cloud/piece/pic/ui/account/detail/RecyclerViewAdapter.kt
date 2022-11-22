package projekt.cloud.piece.pic.ui.account.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import projekt.cloud.piece.pic.databinding.LayoutRecyclerAccountDetailBinding
import projekt.cloud.piece.pic.ui.account.detail.RecyclerViewAdapter.RecyclerViewHolder

class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewHolder>() {

    inner class RecyclerViewHolder(private val binding: LayoutRecyclerAccountDetailBinding)
        : RecyclerView.ViewHolder(binding.root), OnClickListener {
        constructor(context: Context): this(LayoutRecyclerAccountDetailBinding.inflate(LayoutInflater.from(context)))

        init {
            binding.root.setOnClickListener(this)
        }

        fun setDetail(pair: Pair<Int, String>) {
            binding.title = pair.second
            binding.subtitle = binding.root.resources.getString(pair.first)
        }

        override fun onClick(v: View?) {
        }

    }

    var detailList: List<Pair<Int, String>>? = null
        set(value) {
            field = value
            @Suppress("NotifyDataSetChanged")
            notifyDataSetChanged()
        }

    fun setDetails(details: Map<Int, String>) {
        detailList = details.toList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerViewHolder(parent.context)

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        detailList?.get(position)?.let { holder.setDetail(it) }
    }

    override fun getItemCount() = detailList?.size ?: 0

}