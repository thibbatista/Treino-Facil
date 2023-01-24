package com.example.treinofacil.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treinofacil.R
import com.example.treinofacil.databinding.ActivityListTreinoBinding
import com.example.treinofacil.view.model.Treino
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore

class ListTreino : AppCompatActivity() {

    private val liveData = MutableLiveData<List<Treino>>()
    private lateinit var binding: ActivityListTreinoBinding
    private lateinit var treinoList: ArrayList<Treino>
   // private lateinit var recyclerView: RecyclerView
    private lateinit var addtreinoAdapter: AddtreinoAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTreinoBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //recyclerView = findViewById(R.id.rv_treino)
        treinoList = arrayListOf()

        //addtreinoAdapter = AddtreinoAdapter(treinoList)

        insertListeners()


       liveData.observe(this) {

            addtreinoAdapter = AddtreinoAdapter(it)
            binding.rvTreino.layoutManager = LinearLayoutManager(this)
            binding.rvTreino.adapter = addtreinoAdapter

           addtreinoAdapter.onItemClick = { documentId ->

               val intent = Intent(this, MeusExercicios::class.java)
               intent.putExtra("treino", documentId)
               startActivity(intent)
           }

        }

    }

    private fun getDb(){

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("db", "${document.id} => ${document.data}")
                    val treino: Treino? = document.toObject(Treino::class.java)
                    if (treino != null) {
                        treinoList.add(treino)

                       liveData.postValue(treinoList)
                        println("POST VALUE  LISTA CHEIA= ${liveData.value}")

                        updateList()
//                        val id: String = document.id
//                        println(id)
                    }
                }
//                recyclerView.adapter = AddtreinoAdapter(treinoList)
//
//                addtreinoAdapter.onItemClick = {
//
//                    val intent = Intent(this,AddTreino::class.java)
//                    intent.putExtra("treino", it)
//                    startActivity(intent)
//                }
//
            }
            .addOnFailureListener { exception ->
                Log.w("db", "Error getting documents.", exception)
            }
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener {
            val intent = (Intent(this, AddTreino::class.java))
            startActivity(intent)
        }
    }

    private fun updateList() {

        binding.includeEmpty.emptyState.visibility = if (treinoList.isNotEmpty()) View.GONE
        else View.VISIBLE
        addtreinoAdapter.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        treinoList.clear()
        liveData.postValue(treinoList)
        println("POST VALUE  LISTA VAZIA= ${liveData.value}")

        getDb()
    }
}
