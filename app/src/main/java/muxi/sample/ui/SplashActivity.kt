package muxi.sample.ui

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_splash.*
import muxi.sample.R

class SplashActivity: BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splash_animation.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                val splashScreenDuration = getSplashScreenDuration()
                Handler().postDelayed(
                    {
                        startActivity(Intent(this@SplashActivity,ChooseTypeActivity::class.java))
                        finish()
                    },
                    splashScreenDuration
                )
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })

    }

    private fun getSplashScreenDuration() = 300L


}