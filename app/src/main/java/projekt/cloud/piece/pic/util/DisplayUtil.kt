package projekt.cloud.piece.pic.util

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type

object DisplayUtil {

    @JvmStatic
    val Context.deviceBounds: Rect
        get() = (getSystemService(WINDOW_SERVICE) as WindowManager)
            .getDeviceBounds()

    private fun WindowManager.getDeviceBounds(): Rect = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> currentWindowMetrics.bounds
        else -> DisplayMetrics().run {
            @Suppress("DEPRECATION")
            defaultDisplay.getMetrics(this)
            Rect(0, 0, widthPixels, heightPixels)
        }
    }

    @JvmStatic
    fun View.getWindowInsets(block: (Insets) -> Unit) =
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            block.invoke(insets.getInsets(Type.statusBars()))
            WindowInsetsCompat.CONSUMED
        }

}