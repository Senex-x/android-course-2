package com.senex.androidlab1.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.senex.androidlab1.databinding.ActivityWakeBinding

class WakeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWakeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWakeBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}