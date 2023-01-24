package com.example.treinofacil.view.ui

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.treinofacil.databinding.ItemTreinoBinding
import com.example.treinofacil.view.extensions.format
import com.example.treinofacil.view.model.Treino
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class AddtreinoAdapter( private val treinoList: List<Treino>) : RecyclerView.Adapter<TreinoViewHolder>()  {

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

        val data = treinoList[position].data


        fun getReadableDateTime(date: Date): String {
            return SimpleDateFormat("dd/M/yyyy - k:mm ", Locale.getDefault()).format(date)
        }


        holder.data.text = data?.let { getReadableDateTime(it) }

        holder.itemView.setOnClickListener {

            println("DATAFORMATO= $data")
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
