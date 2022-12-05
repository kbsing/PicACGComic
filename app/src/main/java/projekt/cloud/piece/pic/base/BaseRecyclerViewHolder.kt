package projekt.cloud.piece.pic.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseRecyclerViewHolder(view: View): RecyclerView.ViewHolder(view) {
    
    protected val context: Context
        get() = itemView.context
    
}