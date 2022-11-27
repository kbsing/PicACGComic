package projekt.cloud.piece.pic.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object FragmentUtil {

    @Suppress("UNCHECKED_CAST")
    fun <A: FragmentActivity> Fragment.activityAs() = requireActivity() as A

    fun Fragment.setSupportActionBar(toolbar: Toolbar) =
        activityAs<AppCompatActivity>().setSupportActionBar(toolbar)

}