package com.example.agroecologico.data

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class UsuarioData(val nombres: String,
                       val docuemnto: String,
                       val correo: String,
                       val rol: String,
                       val telefono: String,
                       val direccion: String ="NA",
                       val whatsapp: String = "NA",
                       val telegran: String = "NA") {}