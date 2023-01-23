package com.example.treinofacil.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.treinofacil.R
import com.example.treinofacil.databinding.ActivityAddTreinoBinding
import com.example.treinofacil.view.extensions.format
import com.example.treinofacil.view.extensions.text
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.DateTime
import java.sql.Timestamp
import java.util.*

class AddTreino : AppCompatActivity() {

    private lateinit var binding: ActivityAddTreinoBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTreinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        insertListeners()
    }

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
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.tilHour.text = "$hour:$minute"
            }

            timePicker.show(supportFragmentManager, null)
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnNewTask.setOnClickListener {



            val usuariosMap = hashMapOf(
                "nome" to "${binding.tilTitle.text}",
                "descricao" to "${binding.tilDescricao.text}",
                "data" to FieldValue.serverTimestamp()
            )

            db.collection("users")
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