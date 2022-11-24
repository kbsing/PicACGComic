package projekt.cloud.piece.pic.ui.account.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import projekt.cloud.piece.pic.R
import projekt.cloud.piece.pic.base.BaseFragment
import projekt.cloud.piece.pic.ui.account.AccountFragment

open class BaseAccountFragment: BaseFragment() {

    private val accountFragment: AccountFragment
        get() {
            var containerFragment = requireParentFragment()
            while (containerFragment !is AccountFragment) {
                containerFragment = containerFragment.requireParentFragment()
            }
            return containerFragment
        }

    protected fun transactionTo(fragment: Fragment, enterTransition: Any) {
        fragment.enterTransition = enterTransition
        accountFragment.childFragmentManager.commit {
            replace(R.id.fragment_container_view, fragment)
        }
    }

}