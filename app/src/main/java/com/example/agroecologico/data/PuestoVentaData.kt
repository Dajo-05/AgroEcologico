package com.example.pruebaapp.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class PuestoVentaData (val idpuesto: String, val nombrePuesto: String, val telefono: String,
                            val correo: String, val whatsapp: String = "NA", val telegran: String = "NA",
                            val foto: String = "gs://agroecologico-46042.appspot.com/defaulf/campo.jpg") {}