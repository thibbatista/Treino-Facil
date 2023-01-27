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
import com.example.treinofacil.view.model.Treino
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddtreinoAdapter(private val treinoList: ArrayList<Treino>) :
    RecyclerView.Adapter<TreinoViewHolder>() {
    var onItemClick: ((String) -> Unit)? = null

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
                holder.showPopup(holder.ivMore, position, treinoList, this,
                    it1
                )
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


    fun showPopup(view: View, position: Int, list: ArrayList<Treino>, adapter: AddtreinoAdapter,id: String) {
        val popup = PopupMenu(view.context, view)
        popup.inflate(R.menu.popup_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->

            if (item != null) {
                when (item.itemId) {
                    R.id.action_edit -> println("Touch em Edit")
                    R.id.action_delete -> removedItem(position, list, adapter, id)
                }
            }
            true
        }
        popup.show()
    }

    private fun removedItem(position: Int, list: ArrayList<Treino>, adapter: AddtreinoAdapter, id:String) {

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        list.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position,list.size)
        adapter.notifyDataSetChanged()

        if (userId != null) {
            db.collection("users").document(userId).collection("treinos").document(id)
                .delete()
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        }

    }
}




