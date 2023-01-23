package com.example.treinofacil.view.treinos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treinofacil.R
import com.example.treinofacil.view.model.Exercicio
import com.google.firebase.firestore.FirebaseFirestore

class LIstExercicios : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var exercicioList : ArrayList<Exercicio>
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_exercicios)


        recyclerView = findViewById(R.id.rv_exercicio)
        recyclerView.layoutManager = LinearLayoutManager(this)
        exercicioList = arrayListOf()


        db.collection("exercicios")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("db", "${document.id} => ${document.data}")
                    val exercicio : Exercicio? = document.toObject(Exercicio::class.java)
                    if (exercicio != null) {
                        exercicioList.add(exercicio)
                    }
                }
                recyclerView.adapter = ListExerciciosAdapter(exercicioList)
            }
            .addOnFailureListener { exception ->
                Log.w("db", "Error getting documents.", exception)
            }

        //db = FirebaseFirestore.getInstance()
//        db.collection("exercicios").get().addOnSuccessListener {
//
//            println("Odb é resultado=====>>>>${db}")
//            if (!it.isEmpty){
//                for (data in it.documents){
//                    val exercicio : Exercicio? = data.toObject(Exercicio::class.java)
//                    if (exercicio != null){
//                        exercicioList.add(exercicio)
//                    }
//                }
//                recyclerView.adapter = ListExerciciosAdapter(exercicioList)
//            }
//
//            println("Os itens da lista são=====>>>>>>>>${exercicioList}")
//
//        }.addOnFailureListener{
//            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
//        }

    }
}