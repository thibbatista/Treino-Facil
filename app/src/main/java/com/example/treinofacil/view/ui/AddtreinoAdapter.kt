package com.example.treinofacil.view.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.treinofacil.databinding.ItemTreinoBinding
import com.example.treinofacil.view.model.Treino

class AddtreinoAdapter( private val treinoList: ArrayList<Treino>) : RecyclerView.Adapter<TreinoViewHolder>()  {

    var onItemClick : ((Int) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreinoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTreinoBinding.inflate(inflater, parent, false)
        return TreinoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return treinoList.size
    }

    override fun onBindViewHolder(holder: TreinoViewHolder, position: Int) {
        holder.nome.text = treinoList[position].nome
        holder.descricao.text = treinoList[position].descricao
        //holder.data.text = treinoList[position].data.toString()

        holder.itemView.setOnClickListener {
            Log.d("TESTE DE CLICK", "TESTE CLICK RECYCLER VIEW")
            onItemClick?.invoke(position)

        }

    }

}


class TreinoViewHolder(binding: ItemTreinoBinding) : RecyclerView.ViewHolder(binding.root) {

    val nome = binding.tvTitle
    val descricao = binding.tvDescricao
    val data = binding.tvDate



}
