package com.manriquetavi.jetregisterapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.manriquetavi.jetregisterapp.domain.model.Form
import com.manriquetavi.jetregisterapp.domain.model.Response
import com.manriquetavi.jetregisterapp.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): FirestoreRepository {
    override fun addInformation(form: Form): Flow<Response<Void?>> = flow {
        try {
            emit(Response.Loading)
            val formId = firestore.collection("form").document().id
            val addition = firestore
                .collection("form")
                .document(formId).set(form.apply { id = formId }).await()
            emit(Response.Success(addition))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }

}