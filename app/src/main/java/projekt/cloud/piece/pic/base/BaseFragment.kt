package projekt.cloud.piece.pic.base

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import kotlin.reflect.jvm.jvmName
import projekt.cloud.piece.pic.ApplicationConfigs

open class BaseFragment: Fragment() {
    
    protected class SnackMessage(private val sender: String,
                                 val message: String,
                                 val length: Int,
                                 val snack: Snackbar.() -> Unit) {
        
        fun checkFragment(fragment: Fragment) =
            fragment::class.jvmName != sender
        
    }
    
    protected class Message: ViewModel() {
        private val _message = MutableLiveData<SnackMessage?>()
        val message: LiveData<SnackMessage?>
            get() = _message
        fun putMessage(snackMessage: SnackMessage?) {
            _message.value = snackMessage
        }
    }
    
    private val message: Message by activityViewModels()
    
    protected val applicationConfigs: ApplicationConfigs by activityViewModels()
    
    protected inline fun <reified F: Fragment> findParentAs(): F {
        var parent = requireParentFragment()
        while (parent !is F) {
            parent = parent.requireParentFragment()
        }
        return parent
    }
    
    override fun onStart() {
        super.onStart()
        message.message.observe(viewLifecycleOwner) { snackMessage ->
            if (snackMessage != null && snackMessage.checkFragment(this)) {
                onMessageReceived(snackMessage.message, snackMessage.length, snackMessage.snack)
                message.putMessage(null)
            }
        }
    }
    
    protected fun sendMessage(@StringRes resId: Int, length: Int = LENGTH_SHORT, snack: Snackbar.() -> Unit = {}) =
        sendMessage(getString(resId, length, snack))
    
    protected fun sendMessage(message: String, length: Int = LENGTH_SHORT, snack: Snackbar.() -> Unit = {}) {
        this.message.putMessage(
            SnackMessage(this::class.jvmName, message, length, snack)
        )
    }
    
    protected open fun onMessageReceived(message: String, length: Int, snack: Snackbar.() -> Unit = {}) = Unit
    
}