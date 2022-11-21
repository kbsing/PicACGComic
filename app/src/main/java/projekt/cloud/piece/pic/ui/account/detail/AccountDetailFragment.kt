package projekt.cloud.piece.pic.ui.account.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import projekt.cloud.piece.pic.ApplicationConfigs
import projekt.cloud.piece.pic.databinding.FragmentAccountDetailBinding

class AccountDetailFragment: Fragment() {

    private val applicationConfigs: ApplicationConfigs by viewModels(
        ownerProducer = { requireActivity() }
    )

    private var _binding: FragmentAccountDetailBinding? = null
    private val binding: FragmentAccountDetailBinding
        get() = _binding!!
    private val root get() = binding.root

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAccountDetailBinding.inflate(inflater, container, false)
        return root
    }

}