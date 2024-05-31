package com.manriquetavi.jetregisterapp.domain.repository

import com.manriquetavi.jetregisterapp.domain.model.Form
import com.manriquetavi.jetregisterapp.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    fun addInformation(form: Form): Flow<Response<Void?>>
}