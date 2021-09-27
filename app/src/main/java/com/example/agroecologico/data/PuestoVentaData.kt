package com.example.pruebaapp.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class PuestoVentaData (val idpuesto: String, var nombrePuesto: String, val telefono: String,
                            val correo: String, val whatsapp: String = "NA", val telegran: String = "NA",
                            var foto: String = "gs://agroecologico-46042.appspot.com/defaulf/campo.jpg",
                            var vendedor1: String="Vendedor1", var imgVendedor1: String = "gs://agroecologico-46042.appspot.com/defaulf/avatar.jpg") {}

