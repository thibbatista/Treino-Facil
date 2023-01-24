package com.example.treinofacil.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.treinofacil.databinding.ActivityListExercicioBinding
import com.example.treinofacil.databinding.ActivityListTreinoBinding

class ListExercicio : AppCompatActivity() {

    private lateinit var binding: ActivityListExercicioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListExercicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        val documentId = extras?.getString("treino")
        println("Saida documentID = $documentId")

    }

}