package com.example.treinofacil.view.treinos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.treinofacil.R
import com.example.treinofacil.databinding.ItemCardExerciciosCheckBinding
import com.example.treinofacil.view.model.AddExercicio
import com.example.treinofacil.view.model.Exercicio

class ListExerciciosAdapter(private val exercicioList: List<Exercicio>) :
    RecyclerView.Adapter<MainViewHolder>() {

    private lateinit var addExercicio: AddExercicio

    var onItemId: ((String) -> Unit)? = null
    var onItemCheck: ((Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardExerciciosCheckBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return exercicioList.size

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.nome.text = exercicioList[position].nome.toString()
        //holder.image.tag = exercicioList[position].image

        holder.bind(exercicioList[position])



        holder.checkBox.setOnCheckedChangeListener { compoundButton, b ->
            println("Check->$compoundButton, $b")

            addExercicio = AddExercicio(exercicioList[position].documentId)

            onItemCheck?.invoke(b)
            exercicioList[position].documentId?.let { onItemId?.invoke(it) }

        }
    }

}

class MainViewHolder(binding: ItemCardExerciciosCheckBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val nome = binding.titulo

    //val image = binding.ivImage
    val checkBox = binding.checkbox


    private val itemImage = binding.ivImage
    fun bind(exercicio: Exercicio) {
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)

        Glide.with(itemView.context)
            .applyDefaultRequestOptions(requestOptions)
            .load(exercicio.image)
            .transition(DrawableTransitionOptions.withCrossFade(2000))
            .into(itemImage)

    }

}
