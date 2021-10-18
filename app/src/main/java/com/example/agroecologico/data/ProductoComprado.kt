package com.example.agroecologico.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ProductoComprado(val nombreProducto: String = "",
                            val precio: String = "",
                            var cantidad : String = "",
                            val imagProducto: String ="gs://agroecologico-46042.appspot.com/defaulf/proDefault.png",
                            val unidad: String = "",
                            var valtotal: String = ""){}
