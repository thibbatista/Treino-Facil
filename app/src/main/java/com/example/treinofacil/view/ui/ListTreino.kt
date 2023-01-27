package com.example.treinofacil.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treinofacil.R
import com.example.treinofacil.databinding.ActivityListTreinoBinding
import com.example.treinofacil.view.formLogin.FormLogin
import com.example.treinofacil.view.model.Treino
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore

class ListTreino : AppCompatActivity() {

    private val liveData = MutableLiveData<ArrayList<Treino>>()
    private lateinit var binding: ActivityListTreinoBinding
    private  var treinoList = ArrayList<Treino>()
    private lateinit var addtreinoAdapter: AddtreinoAdapter
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

            addtreinoAdapter = AddtreinoAdapter(it)
            binding.rvTreino.layoutManager = LinearLayoutManager(this)
            binding.rvTreino.adapter = addtreinoAdapter

            //lambda evento de click e get documentID do documento treino da collection treino
            addtreinoAdapter.onItemClick = { documentId ->

                val intent = Intent(this, MeusExercicios::class.java)
                intent.putExtra("treino", documentId)
                startActivity(intent)
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
        binding.customToolbar.tvToolbar.text = "meus treinos"

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


    override fun onResume() {
        super.onResume()
        treinoList.clear()
        liveData.postValue(treinoList)
        getDb()
    }
}
