package com.example.treinofacil.view.ui

import android.content.ContentValues.TAG
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.treinofacil.R
import com.example.treinofacil.databinding.ItemCardTreinoBinding
import com.example.treinofacil.view.model.Exercicio
import com.example.treinofacil.view.model.Treino
import com.example.treinofacil.view.treinos.ListExerciciosAdapter
import com.example.treinofacil.view.ui.adapters.MeusExerciciosAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ListTreinoAdapter(private val treinoList: ArrayList<Treino>) :
    RecyclerView.Adapter<TreinoViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    var onItemId: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreinoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardTreinoBinding.inflate(inflater, parent, false)
        return TreinoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return treinoList.size
    }

    override fun onBindViewHolder(holder: TreinoViewHolder, position: Int) {
        holder.nome.text = treinoList[position].nome
        holder.descricao.text = treinoList[position].descricao
        val number = position + 1
        holder.itemNumber.text = number.toString()

        val data = treinoList[position].data


        fun getReadableDateTime(date: Date): String {
            return SimpleDateFormat("dd/M/yyyy - k:mm ", Locale.getDefault()).format(date)
        }

        holder.data.text = data?.let { getReadableDateTime(it) }

        holder.itemView.setOnClickListener {

            println("DATAFORMATO= $data")
            Log.d("TESTE DE CLICK", "TESTE CLICK RECYCLER VIEW")
            treinoList[position].documentId?.let { it1 -> onItemClick?.invoke(it1) }
        }

        holder.ivMore.setOnClickListener {
           treinoList[position].documentId?.let { it1 ->
                onItemId?.let { it2 ->
                    holder.showPopup(
                        holder.ivMore, position,
                        it1, treinoList, this, it2
                    )
                }
            }
        }
    }
}

class TreinoViewHolder(binding: ItemCardTreinoBinding) : RecyclerView.ViewHolder(binding.root) {

    val nome = binding.tvTitle
    val descricao = binding.tvDescricao
    val data = binding.tvDate
    val itemNumber = binding.number
    val ivMore = binding.ivMore


    fun showPopup(
        view: View,
        position: Int,
        idTreino: String,
        list: ArrayList<Treino>,
        adapter: ListTreinoAdapter,
        onItemId: ((String) -> Unit)

    ) {

        val popup = PopupMenu(view.context, view)
        popup.inflate(R.menu.popup_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->

            if (item != null) {
                when (item.itemId) {
                    R.id.action_edit -> println("Touch em Edit")
                    R.id.action_delete -> {
                        onItemId.invoke(idTreino)
                        removedItem(position, list, adapter)
                    }
                }
            }
            true
        }
        popup.show()
    }

    private fun removedItem(
        position: Int,
        list: ArrayList<Treino>,
        adapter: ListTreinoAdapter,
    ) {

        list.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, list.size)
        adapter.notifyDataSetChanged()

    }
}




