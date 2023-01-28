package com.example.treinofacil.view.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.treinofacil.R
import com.example.treinofacil.databinding.ActivityAddTreinoBinding
import com.example.treinofacil.view.extensions.format
import com.example.treinofacil.view.extensions.text
import com.example.treinofacil.view.formLogin.FormLogin
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AddTreino : AppCompatActivity() {


    private lateinit var binding: ActivityAddTreinoBinding
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTreinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        insertListeners()

    }

    //evento de click e get textInput
    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()

            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }


        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute =
                    if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.tilHour.text = "$hour:$minute"
            }

            timePicker.show(supportFragmentManager, null)
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }


        binding.customToolbar.tvToolbar.text = getString(R.string.adicione_treino)

        binding.customToolbar.btnLogout.setOnClickListener {

            auth.signOut()
            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
            finish()
            true
        }

        binding.btnNewTask.setOnClickListener {

            val tilDate = binding.tilDate.text
            val tilHour = binding.tilHour.text
            val tilTitle = binding.tilTitle.text
            val tilDescricao = binding.tilDescricao.text

            if (tilTitle.isEmpty() || tilDescricao.isEmpty() || tilDate.isEmpty() || tilHour.isEmpty()){
                val snackbar = Snackbar.make(it, "Preencha todos os Campos!", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()

            }else{

                //get data e hora e converte em Date
                val formatter = SimpleDateFormat("dd/M/yyyy hh:mm")
                val dateInString = "$tilDate $tilHour"
                val date: Date = formatter.parse(dateInString) as Date
                println("DATA $date")

                val usuariosMap = hashMapOf(
                    "nome" to binding.tilTitle.text,
                    "descricao" to binding.tilDescricao.text,
                    "data" to date
                )

                //grava no firestore
                userId?.let { it1 ->
                    db.collection("users").document(it1.uid).collection("treinos")
                        .add(usuariosMap)
                        .addOnSuccessListener { documentReference ->
                            Log.d("db", "DocumentSnapshot added with ID: ${documentReference.id}")

                            finish()
                        }
                        .addOnFailureListener { e ->
                            Log.w("db", "Error adding document", e)
                        }
                }

            }

        }
    }
}