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

}