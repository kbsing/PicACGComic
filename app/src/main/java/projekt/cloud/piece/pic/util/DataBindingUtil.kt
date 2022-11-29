package projekt.cloud.piece.pic.util

import android.graphics.Bitmap
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter

object DataBindingUtil {

    @JvmStatic
    @BindingAdapter("bitmap")
    fun AppCompatImageView.setImage(bitmap: Bitmap?) {
        setImageBitmap(bitmap)
    }
    
    @JvmStatic
    @BindingAdapter("circleBitmap")
    fun AppCompatImageView.setCircleBitmap(bitmap: Bitmap?) {
        bitmap?.let { setImageDrawable(CircularCroppedDrawable(it)) }
    }

}