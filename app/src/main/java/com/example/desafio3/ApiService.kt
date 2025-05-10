package com.example.desafio3

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("recursos")
    fun obtenerRecursos(): Call<List<Recurso>>

    @GET("recursos/{id}")
    fun obtenerRecursoPorId(@Path("id") id: String): Call<Recurso>

    @POST("recursos")
    fun crearRecurso(@Body recurso: Recurso): Call<Recurso>

    @PUT("recursos/{id}")
    fun actualizarRecurso(@Path("id") id: String, @Body recurso: Recurso): Call<Recurso>

    @DELETE("recursos/{id}")
    fun eliminarRecurso(@Path("id") id: String): Call<Void>
}