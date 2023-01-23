package com.example.treinofacil.view.treinos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.treinofacil.R
import com.example.treinofacil.databinding.ActivityFormLoginBinding
import com.example.treinofacil.databinding.ActivityTreinoBinding
import com.example.treinofacil.view.formLogin.FormLogin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Treino : AppCompatActivity() {
    private lateinit var binding: ActivityTreinoBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTreinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.deslogar.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
            finish()
        }

        binding.gravarDados.setOnClickListener {

            val usuariosMap = hashMapOf(
                "nome" to "Thiago",
                "sobrenome" to "Batista",
                "ideda" to 38
            )

            db.collection("users")
                .add(usuariosMap)
                .addOnSuccessListener { documentReference ->
                    Log.d("db", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("db", "Error adding document", e)
                }
        }

        binding.lerDados.setOnClickListener {

            db.collection("exercicios")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("db", "${document.id} => ${document.data}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("db", "Error getting documents.", exception)
                }

        }
//
//        binding.atualizar.setOnClickListener {
//            db.collection("users")
//                .document("it")
//                .update("nome", "José")
//                .addOnSuccessListener { documentReference ->
//                    Log.d("db", "DocumentSnapshot added with ID: ${documentReference.id}")
//                }
//                .addOnFailureListener { e ->
//                    Log.w("db", "Error adding document", e)
//                }
//
//        }

    }
}