package projekt.cloud.piece.pic.ui.account.login.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputLayout
import projekt.cloud.piece.pic.R
import projekt.cloud.piece.pic.databinding.FragmentLoginBinding

class LoginFragment: Fragment() {

    private companion object {
        val EMAIL_REGEX = "([0-9a-zA-Z]+.)*[0-9a-zA-Z]+@([0-9a-zA-Z].)+.([0-9a-zA-Z])+".toRegex()
        val regex = "[0-9a-zA-Z._]+".toRegex()
        const val PASSWORD_MIN_LENGTH = 8
        const val PASSWORD_MAX_LENGTH = 16
        const val EMPTY_STR = ""
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!
    private val root get() = binding.root
    private val linearProgressIndicator: LinearProgressIndicator
        get() = binding.linearProgressIndicator
    private val account: TextInputLayout
        get() = binding.textInputLayoutAccount
    private val password: TextInputLayout
        get() = binding.textInputLayoutPassword
    private val login: MaterialButton
        get() = binding.materialButtonLogin

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val inputFilters = arrayOf(
            InputFilter { source, start, end, dest, dstart, dend ->
                when {
                    source.matches(regex) -> source
                    else -> EMPTY_STR
                }
            },
            InputFilter.LengthFilter(PASSWORD_MAX_LENGTH)
        )

        var accountIcon = R.drawable.ic_round_account_circle_24
        account.editText?.let { editText ->
            editText.filters = inputFilters
            editText.addTextChangedListener {
                val drawableId = when {
                    it.isEmail -> R.drawable.ic_round_email_24
                    else -> R.drawable.ic_round_account_circle_24
                }
                if (accountIcon != drawableId) {
                    accountIcon = drawableId
                    account.setStartIconDrawable(accountIcon)
                }
                checkIfAccountPasswordFilled()
            }
        }

        with(password) {
            editText?.let { editText ->
                editText.inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
                editText.addTextChangedListener {
                    checkIfAccountPasswordFilled()
                }

                editText.filters = inputFilters

                var isPasswordVisible = false
                setEndIconOnClickListener {
                    isPasswordVisible = !isPasswordVisible

                    val currentCursor = editText.selectionEnd
                    val inputType: Int
                    @DrawableRes val icon: Int
                    when {
                        isPasswordVisible -> {
                            inputType = TYPE_CLASS_TEXT
                            icon = R.drawable.ic_round_visibility_off_24
                        }
                        else -> {
                            inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
                            icon = R.drawable.ic_round_visibility_24
                        }
                    }
                    editText.inputType = inputType
                    editText.setSelection(currentCursor)

                    password.setEndIconDrawable(icon)
                }
            }


        }

        linearProgressIndicator.hide()
        var isLoggingIn = false
        login.setOnClickListener {
            (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(root.windowToken, 0)
            if (isLoggingIn) {
                return@setOnClickListener
            }
            isLoggingIn = true
            linearProgressIndicator.show()
        }
    }

    private val Editable?.isEmail: Boolean
        get() = !this.isNullOrBlank() && this.matches(EMAIL_REGEX)

    private fun checkIfAccountPasswordFilled() {
        login.isEnabled = checkIfFilled(account.editText?.toString(), password.editText?.toString())
    }

    private fun checkIfFilled(account: String?, password: String?) =
        !account.isNullOrBlank() && !password.isNullOrBlank() && password.length >= PASSWORD_MIN_LENGTH

}