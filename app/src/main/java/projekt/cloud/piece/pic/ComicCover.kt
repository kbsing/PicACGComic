package projekt.cloud.piece.pic

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ComicCover: ViewModel() {
    
    private val _cover = MutableLiveData<Bitmap?>()
    val cover: LiveData<Bitmap?>
        get() = _cover
    fun setCover(bitmap: Bitmap?) {
        _cover.value = bitmap
    }
    
}