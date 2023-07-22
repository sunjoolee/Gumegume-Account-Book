package gumegumeCompany.gumegume_account_book

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.zoyi.channel.plugin.android.ChannelIO
import com.zoyi.channel.plugin.android.open.config.BootConfig
import com.zoyi.channel.plugin.android.open.model.Profile
import gumegumeCompany.gumegume_account_book.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object{
        var categoryColorIds = arrayListOf(
            R.color.pastel_rainbow1, //월급&용돈
            R.color.pastel_rainbow2, //고정지출
            R.color.pastel_rainbow3, //식비
            R.color.pastel_rainbow4, //교통비
            R.color.pastel_rainbow5, //생필품
            R.color.pastel_rainbow6, //선물
            R.color.pastel_rainbow7) //기타
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ChannelIO.initialize(application)


/*
        채널톡 멤버 Id 관련해서 보류
        val profile = Profile.create()
            .setName(userName)
            .setEmail(userEmail)
            .setProperty("homepageUrl", "channel.io")

        val bootConfig = BootConfig.create(R.string.YOUR_PLUGIN_KEY.toString())
            .setMemberId(memberId)
            .setProfile(profile)

        ChannelIO.boot(bootConfig)*/

        onSetUpNavigation()
    }

    private fun onSetUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.apply {
            setupWithNavController(navController)
            setOnItemSelectedListener { item ->
                NavigationUI.onNavDestinationSelected(item,navController)
                navController.popBackStack(item.itemId, inclusive = false)
                true
            }
        }
    }
}