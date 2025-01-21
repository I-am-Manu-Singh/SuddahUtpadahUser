package com.neatroots.suddahutpadah.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.neatroots.suddahutpadah.databinding.ActivityHomeMainBinding

class HomeMainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeMainBinding.inflate(layoutInflater) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.WHITE)
        )

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
//            button.setOnClickListener {
//                Firebase.auth.signOut()
//                startActivity(Intent(this@HomeMainActivity, WelcomeActivity::class.java))
//                finish()
//            }
        }

    }

}