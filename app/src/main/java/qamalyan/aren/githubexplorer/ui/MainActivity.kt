package qamalyan.aren.githubexplorer.ui

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import qamalyan.aren.githubexplorer.common.utils.manager.LightSystemBarsManager
import qamalyan.aren.githubexplorer.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LightSystemBarsManager {
    override val contentWindow: Window? by lazy { window }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}