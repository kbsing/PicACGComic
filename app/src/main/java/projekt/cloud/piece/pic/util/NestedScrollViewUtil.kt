package projekt.cloud.piece.pic.util

import androidx.core.widget.NestedScrollView

object NestedScrollViewUtil {
    
    @JvmStatic
    val NestedScrollView.isScrollable: Boolean
        get() = canScrollHorizontally(1) || canScrollVertically(-1)
    
}