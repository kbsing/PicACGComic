package projekt.cloud.piece.pic.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import projekt.cloud.piece.pic.ApplicationConfigs

open class BaseFragment: Fragment() {
    
    protected val applicationConfigs: ApplicationConfigs by activityViewModels()
    
}