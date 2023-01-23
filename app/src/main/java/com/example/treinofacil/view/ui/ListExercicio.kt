package com.example.treinofacil.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.treinofacil.databinding.ActivityListTreinoBinding

class ListExercicio : AppCompatActivity() {

    private lateinit var binding: ActivityListTreinoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTreinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private fun insertListeners() {
        binding.fab.setOnClickListener {
            val intent = (Intent(this, AddTreino::class.java))
        }

    }
}