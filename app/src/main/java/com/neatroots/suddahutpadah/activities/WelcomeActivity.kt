package com.neatroots.suddahutpadah.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import com.neatroots.suddahutpadah.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityWelcomeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.WHITE)
        )


       binding.btStart.setOnClickListener {
           startActivity(Intent(this@WelcomeActivity, AuthActivity::class.java))
           finish()
       }

    }
}