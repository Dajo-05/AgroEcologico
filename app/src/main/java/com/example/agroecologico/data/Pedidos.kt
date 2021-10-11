package com.example.agroecologico.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Pedidos(val cliente: String = "",
                   val correo: String = "",
                   val direccion: String = "",
                   val entrega: String = "",
                   val telefono: String = "",
                   val total: String = "",
                   val compra: MutableList<ProductoComprado> = arrayListOf()): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {}

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Pedidos> {
        override fun createFromParcel(parcel: Parcel): Pedidos {
            return Pedidos(parcel)
        }

        override fun newArray(size: Int): Array<Pedidos?> {
            return arrayOfNulls(size)
        }
    }
}
