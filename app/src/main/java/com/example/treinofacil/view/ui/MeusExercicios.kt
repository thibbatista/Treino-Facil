package com.example.treinofacil.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.treinofacil.databinding.ActivityMeusExerciciosBinding

class MeusExercicios : AppCompatActivity() {

    private lateinit var binding: ActivityMeusExerciciosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeusExerciciosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        val documentId = extras?.getString("treino")
        println("Saida documentID = $documentId")


    }

}