package com.example.treinofacil.view.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.treinofacil.R
import com.example.treinofacil.databinding.ActivityListTreinoBinding
import com.example.treinofacil.view.formLogin.FormLogin
import com.example.treinofacil.view.model.Treino
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ListTreino : AppCompatActivity() {

    private val liveData = MutableLiveData<ArrayList<Treino>>()
    private lateinit var binding: ActivityListTreinoBinding
    private var treinoList = ArrayList<Treino>()
    private lateinit var listTreinoAdapter: ListTreinoAdapter
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTreinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        insertListeners()

        //Observer  inicializa recycler e adapter
        liveData.observe(this) {

            listTreinoAdapter = ListTreinoAdapter(it)
            binding.rvTreino.layoutManager = LinearLayoutManager(this)
            binding.rvTreino.adapter = listTreinoAdapter

            //lambda evento de click e get documentID do documento treino da collection treino
            listTreinoAdapter.onItemClick = { documentId ->

                val intent = Intent(this, MeusExercicios::class.java)
                intent.putExtra("treino", documentId)
                startActivity(intent)
            }

            listTreinoAdapter.onItemId = {idTreino->
                userId?.let { it1 -> removedItem(it1.uid, idTreino) }

            }
        }
    }

    private fun getDb() {

        userId?.let {
            db.collection("users").document(it.uid).collection("treinos")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("db", "${document.id} => ${document.data}")
                        val treino: Treino? = document.toObject(Treino::class.java)
                        if (treino != null) {
                            treinoList.add(treino)
                            liveData.postValue(treinoList)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("db", "Error getting documents.", exception)
                }
        }
    }

    private fun insertListeners() {

        //setText toolBar
        binding.customToolbar.tvToolbar.text = getString(R.string.meus_treinos)

        //singOut
        binding.customToolbar.btnLogout.setOnClickListener {

            auth.signOut()
            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
            finish()
            true
        }

        //botao adicionar treino
        binding.fab.setOnClickListener {
            val intent = (Intent(this, AddTreino::class.java))
            startActivity(intent)
        }
    }


    private fun removedItem(
        userId: String,
        id: String
    ) {

        db.collection("users").document(userId).collection("treinos").document(id)
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


    override fun onResume() {
        super.onResume()
        treinoList.clear()
        liveData.postValue(treinoList)
        getDb()
    }
}
