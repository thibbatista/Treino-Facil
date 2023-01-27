package com.example.treinofacil.view.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.treinofacil.databinding.ActivityMeusExerciciosBinding
import com.example.treinofacil.view.formLogin.FormLogin
import com.example.treinofacil.view.model.Exercicio
import com.example.treinofacil.view.ui.adapters.MeusExerciciosAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MeusExercicios : AppCompatActivity() {

    private val liveData = MutableLiveData<ArrayList<Exercicio>>()
    private lateinit var binding: ActivityMeusExerciciosBinding


    private  var exercicioList = ArrayList<Exercicio>()
    private lateinit var meusExerciciosAdapter: MeusExerciciosAdapter
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser
    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeusExerciciosBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //getExtras
        val extras = intent.extras
        val documentId = extras?.getString("treino")
        println("Saida documentID = $documentId")


        //Observer, recebe Arraylist de exercicios
        liveData.observe(this) {
            meusExerciciosAdapter = MeusExerciciosAdapter(it)
            binding.rvMeusExercicios.layoutManager = LinearLayoutManager(this)
            binding.rvMeusExercicios.adapter = meusExerciciosAdapter

            //lambda click item menu delete , retorna id do exercicio
            meusExerciciosAdapter.onItemId = { id ->
                Log.d("onItemId", "ID -> $id")


                //get lista de exercicios do usuario para compara com id da lista de exercicios geral
                userId?.let { it1 ->
                    if (documentId != null) {
                        db.collection("users").document(it1.uid).collection("treinos")
                            .document(documentId).collection("exercicios")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    Log.d("List exercicios", "${document.id} => ${document.data}")
                                    for (d in document.data) {
                                        val value = d.value.toString()
                                        if (id == value) {
                                            Log.d("RemoveID", "id->${document.id}")


                                            //Remove o document.id do firestore
                                            db.collection("users").document(it1.uid)
                                                .collection("treinos").document(documentId)
                                                .collection("exercicios").document(document.id)
                                                .delete()
                                                .addOnSuccessListener {
                                                    Log.d(
                                                        ContentValues.TAG,
                                                        "DocumentSnapshot successfully deleted $id! "
                                                    )
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.w(
                                                        ContentValues.TAG,
                                                        "Error deleting document",
                                                        e
                                                    )
                                                }
                                        }
                                    }
                                }
                            }

                    }

                }
            }
        }


        //setText Toolbar
        binding.customToolbar.tvToolbar.text = "meus exercicios"


        //signOut
        binding.customToolbar.btnLogout.setOnClickListener {

            auth.signOut()
            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
            finish()
            true
        }


        //get lista de exercicios do usuario e adiciona como objeto em exerciciosList em liveData
        if (documentId != null) {
            userId?.let {
                db.collection("users").document(it.uid).collection("treinos")
                    .document(documentId).collection("exercicios")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d("db exercicios", "${document.id} => ${document.data}")
                            for (d in document.data) {
                                val id = d.value.toString()

                                //get exercicio atraves do nome de referencia
                                db.collection("exercicios").document(id)
                                    .get()
                                    .addOnSuccessListener { result2 ->
                                        val exercicio: Exercicio? =
                                            result2.toObject(Exercicio::class.java)
                                        if (exercicio != null) {
                                            exercicioList.add(exercicio)
                                            liveData.postValue(exercicioList)
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.w("db", "Error getting documents.", exception)
                                    }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("db", "Error getting documents.", exception)
                    }
            }
        }


        // evento de click no botao adicionar exercicios, passa o id elemento treino para activity listExercicios
        binding.addExercicio.setOnClickListener {
            val intent = Intent(this, LIstExercicios::class.java)
            intent.putExtra("treino", documentId)
            startActivity(intent)
            finish()
        }
    }
}