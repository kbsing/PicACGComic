package projekt.cloud.piece.pic

import android.content.Context
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import projekt.cloud.piece.pic.api.ApiAuth.SignInResponseBody
import projekt.cloud.piece.pic.api.ApiAuth.signIn
import projekt.cloud.piece.pic.util.CoroutineUtil.io
import projekt.cloud.piece.pic.util.CoroutineUtil.ui
import projekt.cloud.piece.pic.util.HttpUtil.RESPONSE_CODE_SUCCESS
import projekt.cloud.piece.pic.util.StorageUtil.Account
import projekt.cloud.piece.pic.util.StorageUtil.readAccount

class ApplicationConfigs: ViewModel() {

    fun initializeAccount(context: Context) {
        viewModelScope.launch(io) {
            context.readAccount()?.let { account ->
                ui { setAccount(account) }
                signIn(account.account, account.password)?.let { response ->
                    if (response.code == RESPONSE_CODE_SUCCESS) {
                        ui {
                            updateToken(
                                Json.decodeFromString<SignInResponseBody>(response.body.string())
                                    .token
                            )
                        }
                    }
                }
            }
        }
    }

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
    val token: LiveData<String?>
        get() = _token
    fun updateToken(token: String?) {
        _token.value = token
    }

}