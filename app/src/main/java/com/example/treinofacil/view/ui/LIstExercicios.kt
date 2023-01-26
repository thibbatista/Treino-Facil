package com.example.treinofacil.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treinofacil.R
import com.example.treinofacil.databinding.ActivityListExerciciosBinding
import com.example.treinofacil.databinding.ActivityListTreinoBinding
import com.example.treinofacil.view.model.AddExercicio
import com.example.treinofacil.view.model.Exercicio
import com.example.treinofacil.view.model.Treino
import com.example.treinofacil.view.treinos.ListExerciciosAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LIstExercicios : AppCompatActivity() {

    private val liveData = MutableLiveData<List<Exercicio>>()
    private lateinit var binding: ActivityListExerciciosBinding
   //private lateinit var recyclerView : RecyclerView
    private lateinit var exercicioList : ArrayList<Exercicio>
    private lateinit var listExerciciosAdapter: ListExerciciosAdapter
    private val db = FirebaseFirestore.getInstance()
    private val listAddExercicios = ArrayList<String>()
    private val userId = FirebaseAuth.getInstance().currentUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListExerciciosBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //getExtras

        val extras = intent.extras
        val documentId = extras?.getString("treino")
        println("Saida documentID = $documentId")



        liveData.observe(this) {

            listExerciciosAdapter = ListExerciciosAdapter(it)
            binding.rvExercicio.layoutManager = LinearLayoutManager(this)
            binding.rvExercicio.adapter = listExerciciosAdapter

            listExerciciosAdapter.onItemCheck = { check ->



                if (check){
                    //verifica se não possui na lista e adiciona
                    listExerciciosAdapter.onItemId = {id->
                        listAddExercicios.add(id)
                        println("LISTA PARA GRAVAR NO FIRESTORE -> $listAddExercicios")
                    }
                }else{
                    //verifica se possui na lista e remove
                    listExerciciosAdapter.onItemId = {id->
                        listAddExercicios.remove(id)
                        println("LISTA PARA GRAVAR NO FIRESTORE -> $listAddExercicios")
                    }
                }
            }
        }


        binding.btnGravar.setOnClickListener {

            for (i in listAddExercicios){


                val usuariosMap = hashMapOf(
                    "nome" to i

                )

                if (documentId != null) {
                    userId?.let { it1 ->
                        db.collection("users").document(it1.uid).collection("treinos").document(documentId).collection("exercicios")
                            .add(usuariosMap)
                            .addOnSuccessListener { documentReference ->
                                Log.d("db", "DocumentSnapshot added with ID: ${documentReference.id}")

                                val intent = Intent(this, MeusExercicios::class.java)
                                intent.putExtra("treino", documentId)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Log.w("db", "Error adding document", e)
                            }
                    }
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }


//        recyclerView = findViewById(R.id.rv_exercicio)
//        recyclerView.layoutManager = LinearLayoutManager(this)
        exercicioList = arrayListOf()


        db.collection("exercicios")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("db", "${document.id} => ${document.data}")
                    val exercicio : Exercicio? = document.toObject(Exercicio::class.java)
                    if (exercicio != null) {
                        exercicioList.add(exercicio)
                        liveData.postValue(exercicioList)
                    }
                }
                //recyclerView.adapter = ListExerciciosAdapter(exercicioList)
            }
            .addOnFailureListener { exception ->
                Log.w("db", "Error getting documents.", exception)
            }




    }
}