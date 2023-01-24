package com.example.treinofacil.view.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.treinofacil.databinding.ExercicioBinding
import com.example.treinofacil.view.model.AddExercicio
import com.example.treinofacil.view.model.Exercicio

class MeusExerciciosAdapter (private val exercicioList: List<Exercicio>) : RecyclerView.Adapter<MainViewHolder>() {

    //private lateinit var addExercicio: AddExercicio

    //var onItemId : ((String) -> Unit)? = null
    //var onItemCheck : ((Boolean) -> Unit)? = null

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

//
//
//        holder.checkBox.setOnCheckedChangeListener { compoundButton, b ->
//            println("Check->$compoundButton, $b")
//
//            addExercicio = AddExercicio(exercicioList[position].documentId)
//
//            onItemCheck?.invoke(b)
//            exercicioList[position].documentId?.let { onItemId?.invoke(it) }
//
//        }
    }

}

class MainViewHolder(binding: ExercicioBinding) : RecyclerView.ViewHolder(binding.root) {

    val nome = binding.titulo
    val image = binding.image
    val obs = binding.obs
    val checkBox = binding.checkbox

}
