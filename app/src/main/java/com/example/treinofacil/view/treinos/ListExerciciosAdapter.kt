package com.example.treinofacil.view.treinos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.treinofacil.databinding.ExercicioBinding
import com.example.treinofacil.view.model.Exercicio

class ListExerciciosAdapter(private val exercicioList: ArrayList<Exercicio>) : RecyclerView.Adapter<MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ExercicioBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return exercicioList.size

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.nome.text = exercicioList[position].nome.toString()
        holder.obs.text = exercicioList[position].observacao
        holder.image.tag = exercicioList[position].image
    }

}

class MainViewHolder(binding: ExercicioBinding) : RecyclerView.ViewHolder(binding.root) {

    val nome = binding.titulo
    val image = binding.image
    val obs = binding.obs

}
