package projekt.cloud.piece.pic

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import projekt.cloud.piece.pic.util.StorageUtil.Account

class ApplicationConfigs: ViewModel() {

    private val _windowInsetTop = MutableLiveData<Int>()
    val windowInsetTop: LiveData<Int>
        get() = _windowInsetTop

    private val _windowInsetBottom = MutableLiveData<Int>()
    val windowInsetBottom: LiveData<Int>
        get() = _windowInsetBottom

    fun setUpWindowInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            _windowInsetTop.value = insets.getInsets(Type.statusBars()).top
            _windowInsetBottom.value = insets.getInsets(Type.navigationBars()).bottom
            WindowInsetsCompat.CONSUMED
        }
    }

    private val _account = MutableLiveData<Account?>()
    val account: LiveData<Account?>
        get() = _account
    fun setAccount(account: Account?) {
        this._account.value = account
    }

    private val _token = MutableLiveData<String?>()
    private val token: LiveData<String?>
        get() = _token
    fun updateToken(token: String?) {
        _token.value = token
    }

}