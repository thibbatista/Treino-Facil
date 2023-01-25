package com.example.treinofacil.view.formCadastro

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.treinofacil.R
import com.example.treinofacil.databinding.ActivityFormCadastroBinding
import com.example.treinofacil.view.formLogin.FormLogin
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.ktx.Firebase

class FormCadastro : AppCompatActivity() {

    private lateinit var binding: ActivityFormCadastroBinding
    private var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btCadastrar.setOnClickListener{ view ->

            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            if (email.isEmpty() || senha.isEmpty()){
                val snackbar =  Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            }else{
                auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener{ cadastro ->
                    if (cadastro.isSuccessful) {
                        val snackbar = Snackbar.make(
                            view,
                            "Cadastro realizado com sucesso!",
                            Snackbar.LENGTH_SHORT
                        )
                        snackbar.setBackgroundTint(Color.BLUE)
                        snackbar.show()
                        binding.editEmail.setText("")
                        binding.editSenha.setText("")

                        val intent = Intent(this, FormLogin::class.java)
                        startActivity(intent)
                        finish()

                    }
                }.addOnFailureListener{ exception ->

                    val mensagemErro = when(exception){
                        is FirebaseAuthWeakPasswordException -> "Digite no minimo seis caracteres!"
                        is FirebaseAuthInvalidCredentialsException -> "Email inválido!"
                        is FirebaseAuthUserCollisionException -> "Email já cadastrada"
                        is FirebaseNetworkException -> "Sem conexão com a internet!"
                        else -> "Erro ao cadastrar usuário!"
                    }
                    val snackbar =  Snackbar.make(view, mensagemErro, Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()

                }
            }
        }
    }
}