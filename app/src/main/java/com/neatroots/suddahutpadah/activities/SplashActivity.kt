package com.neatroots.suddahutpadah.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import com.neatroots.suddahutpadah.R
import com.neatroots.suddahutpadah.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    private var currentIndex = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var textView: TextView
    private val textToAnimate = "Natural and Organic products"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.WHITE)
        )

        handler.postDelayed({
//            if(Firebase.auth.currentUser != null) {
//                startActivity(Intent(this@SplashActivity, HomeMainActivity::class.java))
//                finish()
//            } else {
//                startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
//                finish()
//            }
            startActivity(Intent(this@SplashActivity, HomeMainActivity::class.java))
            finish()
        },2500)

        val imageView = binding.imageView
        textView = binding.textView

        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.icon_animation)
        imageView.startAnimation(slideAnimation)
        textView.startAnimation(slideAnimation)
        binding.tv1.startAnimation(slideAnimation)
        binding.tv2.startAnimation(slideAnimation)

        animateText()

    }

    private fun animateText() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (currentIndex < textToAnimate.length) {
                    textView.append(textToAnimate[currentIndex].toString())
                    currentIndex++
                    handler.postDelayed(this, 80)
                }
            }
        }, 80)
    }


}