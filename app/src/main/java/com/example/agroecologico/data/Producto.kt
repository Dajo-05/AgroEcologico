package com.example.agroecologico.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Producto(val nombreProducto: String,
                    val precio: Int, val cantidad : Int,
                     val imagProducto: String ="gs://agroecologico-46042.appspot.com/defaulf/proDefault.png")