package projekt.cloud.piece.pic.ui.account.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import projekt.cloud.piece.pic.ApplicationConfigs
import projekt.cloud.piece.pic.R
import projekt.cloud.piece.pic.databinding.FragmentAccountLoginBinding
import projekt.cloud.piece.pic.ui.account.login.login.LoginFragment
import projekt.cloud.piece.pic.ui.account.login.register.RegisterFragment

class AccountLoginFragment: Fragment() {

    private var _binding: FragmentAccountLoginBinding? = null
    private val binding: FragmentAccountLoginBinding
        get() = _binding!!
    private val root get() = binding.root
    private val toolbar: MaterialToolbar
        get() = binding.materialToolbar
    private val tabLayout: TabLayout
        get() = binding.tabLayout
    private val viewPager2: ViewPager2
        get() = binding.viewPager2

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = requireParentFragment().findNavController()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAccountLoginBinding.inflate(inflater, container, false)
        val applicationConfigs: ApplicationConfigs by viewModels(ownerProducer = { requireActivity() })
        binding.applicationConfigs = applicationConfigs
        binding.lifecycleOwner = viewLifecycleOwner
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener { navController.navigateUp() }
        }

        binding.viewPager2.adapter = object: FragmentStateAdapter(this) {
            private val fragments = arrayOf(LoginFragment(), RegisterFragment())
            override fun getItemCount() = fragments.size
            override fun createFragment(position: Int) = fragments[position]
        }
        arrayOf(R.string.account_logout_tab_login, R.string.account_logout_tab_register).let {
            TabLayoutMediator(tabLayout, viewPager2) { tab, pos ->
                tab.setText(it[pos])
            }.attach()
        }
    }

}