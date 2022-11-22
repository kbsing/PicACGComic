package projekt.cloud.piece.pic.util

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat.TRANSPARENT
import android.graphics.Rect
import android.graphics.Shader.TileMode.CLAMP
import android.graphics.drawable.Drawable
import androidx.core.graphics.toRectF

class CircularCroppedDrawable(private val bitmap: Bitmap?): Drawable() {

    private val paint = Paint().apply {
        isAntiAlias = true
    }

    override fun draw(canvas: Canvas) {
        bitmap?.let {
            paint.shader = BitmapShader(bitmap, CLAMP, CLAMP)
            val radius = bounds.height() / 2F
            canvas.drawRoundRect(bounds.toRectF(), radius, radius, paint)
        }
    }

    override fun setBounds(bounds: Rect) {
        bitmap?.let {
            super.setBounds(Rect(0, 0, bitmap.width, bitmap.height))
        }
    }

    override fun setAlpha(alpha: Int) = Unit

    override fun setColorFilter(colorFilter: ColorFilter?) = Unit

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getOpacity() = TRANSPARENT

    override fun getIntrinsicWidth(): Int {
        return bitmap?.width ?: super.getIntrinsicWidth()
    }

    override fun getIntrinsicHeight(): Int {
        return bitmap?.height ?: super.getIntrinsicHeight()
    }

}