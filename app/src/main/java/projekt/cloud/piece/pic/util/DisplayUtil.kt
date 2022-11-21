package projekt.cloud.piece.pic.util

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager

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

}