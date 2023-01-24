package com.example.treinofacil.view.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.treinofacil.databinding.ActivityAddTreinoBinding
import com.example.treinofacil.view.extensions.format
import com.example.treinofacil.view.extensions.text
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
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


            val formatter = SimpleDateFormat("dd/M/yyyy hh:mm")

            val dateInString = "${binding.tilDate.text} ${binding.tilHour.text}"
            val date: Date = formatter.parse(dateInString) as Date
            println("DATA $date")


            val usuariosMap = hashMapOf(
                "nome" to "${binding.tilTitle.text}",
                "descricao" to "${binding.tilDescricao.text}",
                "data" to date
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