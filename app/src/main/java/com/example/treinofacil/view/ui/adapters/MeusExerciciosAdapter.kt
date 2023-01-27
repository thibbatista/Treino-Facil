package com.example.treinofacil.view.ui.adapters

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.treinofacil.R
import com.example.treinofacil.databinding.ItemCardExercicioBinding
import com.example.treinofacil.view.model.Exercicio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MeusExerciciosAdapter(private val exercicioList: ArrayList<Exercicio>) :
    RecyclerView.Adapter<MainViewHolder>() {

    //private lateinit var addExercicio: AddExercicio

    var onItemId : ((String) -> Unit)? = null
//    var onItemCheck : ((Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardExercicioBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return exercicioList.size

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.nome.text = exercicioList[position].nome.toString()
        holder.obs.text = exercicioList[position].observacao
        // holder.image.tag = exercicioList[position].image
        holder.bind(exercicioList[position])


        holder.ivMore.setOnClickListener {
            exercicioList[position].documentId?.let { it1 ->
                onItemId?.let { it2 ->
                    holder.showPopup(holder.ivMore,position,
                        it1, exercicioList, this, it2
                    )
                }
            }
        }

    }

}

class MainViewHolder(binding: ItemCardExercicioBinding) : RecyclerView.ViewHolder(binding.root) {

    val nome = binding.titulo

    //val image = binding.ivImage
    val obs = binding.obs
    val ivMore = binding.ivMore


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


    fun showPopup(
        view: View,
        position: Int,
        idExercicio: String,
        list: ArrayList<Exercicio>,
        adapter: MeusExerciciosAdapter,
        onItemId: ((String) -> Unit)

    ) {

       // var onItemId: ((String) -> Unit)? = null
        val popup = PopupMenu(view.context, view)
        popup.inflate(R.menu.popup_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->

            if (item != null) {
                when (item.itemId) {
                    R.id.action_edit -> println("Touch em Edit")
                    R.id.action_delete -> {
                        onItemId.invoke(idExercicio)
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
        list: ArrayList<Exercicio>,
        adapter: MeusExerciciosAdapter,
    ) {

        list.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, list.size)
        adapter.notifyDataSetChanged()

    }
}
