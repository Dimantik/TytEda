package com.dimantik.tyteda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dimantik.tyteda.databinding.ActivityMainBinding
import com.dimantik.tyteda.ui.menu.dish.DishAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }

}