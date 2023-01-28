package com.example.treinofacil.view.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Treino(

    val nome: String? = null,
    val descricao: String? = null,

    @ServerTimestamp var data: Date? = null,

    @DocumentId
    val documentId: String? = null

)
