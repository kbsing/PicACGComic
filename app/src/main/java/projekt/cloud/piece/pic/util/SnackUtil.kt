package projekt.cloud.piece.pic.util

import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar

object SnackUtil {

    fun CoordinatorLayout.snack(@StringRes resId: Int, length: Int = LENGTH_SHORT) =
        Snackbar.make(this, resId, length)

    fun CoordinatorLayout.snack(message: String, length: Int = LENGTH_SHORT) =
        Snackbar.make(this, message, length)

    fun CoordinatorLayout.showSnack(@StringRes resId: Int, length: Int = LENGTH_SHORT) =
        snack(resId, length).show()

    fun CoordinatorLayout.showSnack(message: String, length: Int = LENGTH_SHORT) =
        snack(message, length).show()

}