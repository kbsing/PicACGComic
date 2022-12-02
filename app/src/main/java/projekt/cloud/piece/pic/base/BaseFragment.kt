package projekt.cloud.piece.pic.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import projekt.cloud.piece.pic.ApplicationConfigs

open class BaseFragment: Fragment() {
    
    protected val applicationConfigs: ApplicationConfigs by activityViewModels()
    
    protected inline fun <reified F: Fragment> findParentAs(): F {
        var parent = requireParentFragment()
        while (parent !is F) {
            parent = parent.requireParentFragment()
        }
        return parent
    }
    
}