package projekt.cloud.piece.pic.ui.read

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReadComic: ViewModel() {
    
    private companion object {
        const val INDEX_INIT_VALUE = -1
    }
    
    var index = INDEX_INIT_VALUE
        @Synchronized
        set(value) {
            val oldField = field
            field = value
            if (oldField != INDEX_INIT_VALUE) {
                when {
                    value > oldField -> _next.value = value
                    value < oldField -> _prev.value = value
                }
            }
        }

    private val _prev = MutableLiveData<Int>()
    val prev: LiveData<Int>
        get() = _prev
    
    private val _next = MutableLiveData<Int>()
    val next: LiveData<Int>
        get() = _next
    
}