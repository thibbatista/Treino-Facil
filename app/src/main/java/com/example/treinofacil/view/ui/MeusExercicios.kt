package com.example.treinofacil.view.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.treinofacil.R
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

        if (documentId != null) {
            getDb(documentId)
            insertListeners(documentId)
        }


        //Observer, recebe Arraylist de exercicios
        liveData.observe(this) {
            meusExerciciosAdapter = MeusExerciciosAdapter(it)
            binding.rvMeusExercicios.layoutManager = LinearLayoutManager(this)
            binding.rvMeusExercicios.adapter = meusExerciciosAdapter

            //lambda click item menu delete , retorna id do exercicio
            meusExerciciosAdapter.onItemId = { id ->
                Log.d("onItemId", "ID -> $id")

                if (documentId != null) {
                    getUserExercicios(documentId,id)
                }
            }
        }
    }

    //get lista de exercicios do usuario e adiciona como objeto em exerciciosList em liveData
    private fun getDb(documentId: String){
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
    }

    //eventos de clique
    private fun insertListeners(documentId: String){


        //setText Toolbar
        binding.customToolbar.tvToolbar.text = getString(R.string.meus_exercicios)

        // evento de click no botao adicionar exercicios, passa o id elemento treino para activity listExercicios
        binding.addExercicio.setOnClickListener {
            val intent = Intent(this, LIstExercicios::class.java)
            intent.putExtra("treino", documentId)
            startActivity(intent)
            finish()
        }

        //signOut
        binding.customToolbar.btnLogout.setOnClickListener {

            auth.signOut()
            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
            finish()

        }
    }


    //get lista de exercicios do usuario para compara com id da lista de exercicios geral
    private fun getUserExercicios (idTreino: String, idExercicio: String){
        userId?.let { user ->
            db.collection("users").document(user.uid).collection("treinos")
                .document(idTreino).collection("exercicios")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("List exercicios", "${document.id} => ${document.data}")
                        for (d in document.data) {
                            val value = d.value.toString()
                            if (idExercicio == value) {
                                Log.d("RemoveID", "id->${document.id}")
                                removeItem(user.uid, document.id, idTreino)
                            }
                        }
                    }
                }
        }
    }

    //Remove o document.id do firestore
    private fun removeItem(idUsuario:String, idDocumento: String, idTreino:String){
        db.collection("users").document(idUsuario)
            .collection("treinos").document(idTreino)
            .collection("exercicios").document(idDocumento)
            .delete()
            .addOnSuccessListener {
                Log.d(
                    ContentValues.TAG,
                    "DocumentSnapshot successfully deleted!"
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