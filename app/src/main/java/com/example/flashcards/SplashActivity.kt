package com.example.flashcards

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.flashcards.databinding.ActivityMainBinding
import com.example.flashcards.databinding.ActivitySplashBinding

lateinit var binding: ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.splashText.visibility = android.view.View.VISIBLE
        binding.splashText.startAnimation(fadeInAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}
