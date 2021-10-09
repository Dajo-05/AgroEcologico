package com.example.agroecologico.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Pedidos(val cliente: String = "",
                   val correo: String = "",
                   val direccion: String = "",
                   val entrega: String = "",
                   val telefono: String = "",
                   val total: String = "",
                   val compra: MutableList<ProductoComprado> = arrayListOf()){}
