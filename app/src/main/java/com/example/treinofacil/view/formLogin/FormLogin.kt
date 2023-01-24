package com.example.treinofacil.view.formLogin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.treinofacil.databinding.ActivityFormLoginBinding
import com.example.treinofacil.view.formCadastro.FormCadastro
import com.example.treinofacil.view.ui.ListTreino
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class FormLogin : AppCompatActivity() {

    private lateinit var binding: ActivityFormLoginBinding
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btLogin.setOnClickListener {view ->

            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            if (email.isEmpty() || senha.isEmpty()){
                val snackbar = Snackbar.make(view, "Preencha todos os Campos!", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()

            }else{

                auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener{ authResult ->

                    if(authResult.isSuccessful){
                        navTreino()
                    }
                }.addOnFailureListener{ exception ->

                    val mensagemErro = when(exception){
                        is FirebaseAuthWeakPasswordException -> "Senha inválida!!"
                        is FirebaseAuthInvalidCredentialsException -> "Email inválido!"
                        is FirebaseAuthUserCollisionException -> "Email inválido"
                        is FirebaseNetworkException -> "Sem conexão com a internet!"
                        else -> "Erro ao cadastrar usuário!"
                    }
                    val snackbar =  Snackbar.make(view, mensagemErro, Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()

                }
            }
        }

        binding.txtCadastre.setOnClickListener {
            val intent = Intent(this, FormCadastro::class.java)
            startActivity(intent)
        }
    }

    private fun navTreino(){
        val intent = Intent(this, ListTreino::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

        val usuarioAtual =  FirebaseAuth.getInstance().currentUser

        if (usuarioAtual != null){
            navTreino()
        }
    }

}