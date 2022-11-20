package projekt.cloud.piece.pic

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import projekt.cloud.piece.pic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        val applicationConfigs: ApplicationConfigs by viewModels()
        applicationConfigs.setUpWindowInsets(window.decorView)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}