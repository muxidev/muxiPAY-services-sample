package muxi.sample.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import muxi.sample.R

class SplashActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        scheduleSplashScreen()
    }

    private fun scheduleSplashScreen() {
        val splashScreenDuration = getSplashScreenDuration()
        Handler().postDelayed(
            {
                startActivity(Intent(this@SplashActivity,ChooseTypeActivity::class.java))
                finish()
            },
            splashScreenDuration
        )
    }

    private fun getSplashScreenDuration() = 2000L


}