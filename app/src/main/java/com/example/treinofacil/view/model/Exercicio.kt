package com.example.treinofacil.view.model

import com.google.firebase.firestore.DocumentId

data class Exercicio(

    val nome: String? = null,
    val image: String? = null,
    val observacao: String? = null,

    @DocumentId
    val documentId: String? = null
)