package projekt.cloud.piece.pic.util

import androidx.recyclerview.widget.RecyclerView

object RecyclerViewUtil {
    
    @Suppress("UNCHECKED_CAST")
    fun <A: RecyclerView.Adapter<*>> RecyclerView.adapterAs(): A =
        adapter as A
    
}