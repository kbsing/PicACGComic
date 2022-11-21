package projekt.cloud.piece.pic.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.transition.platform.MaterialContainerTransform
import projekt.cloud.piece.pic.ApplicationConfigs
import projekt.cloud.piece.pic.R
import projekt.cloud.piece.pic.databinding.FragmentAccountBinding
import projekt.cloud.piece.pic.ui.account.detail.AccountDetailFragment
import projekt.cloud.piece.pic.ui.account.login.AccountLoginFragment

class AccountFragment: Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding: FragmentAccountBinding
        get() = _binding!!
    private val root get() = binding.root

    private val applicationConfigs: ApplicationConfigs by viewModels(
        ownerProducer = { requireActivity() }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val fragment = when (applicationConfigs.token.value) {
            null -> AccountLoginFragment()
            else -> AccountDetailFragment()
        }
        childFragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, fragment)
            .show(fragment)
            .commit()
    }

}