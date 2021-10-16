package com.example.agroecologico.FragmentoComprador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.agroecologico.R
import com.example.agroecologico.data.UsuarioData
import com.example.agroecologico.databinding.FragmentHomeCompradorBinding
import com.example.agroecologico.databinding.FragmentHomeVendedorBinding
import com.example.pruebaapp.data.PuestoVentaData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HomeCompradorFragment : Fragment() {

         private lateinit var mBinding: FragmentHomeCompradorBinding
         private lateinit var database: DatabaseReference
         private lateinit var auth: FirebaseAuth
         private lateinit var usuario: UsuarioData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth= Firebase.auth
        mBinding= FragmentHomeCompradorBinding.inflate(inflater, container, false)
        cargaDatos()

        return mBinding.root
    }


    fun cargaDatos(){
        mBinding.progressBarComprador.visibility = View.VISIBLE
        val userId = auth.currentUser?.uid.toString()
        database = FirebaseDatabase.getInstance().getReference("Usuarios")
        database.addValueEventListener( object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    usuario = UsuarioData(nombres = snapshot.child("${userId}").child("nombres").getValue().toString(),
                                              docuemnto = snapshot.child("${userId}").child("documento").getValue().toString(),
                                              correo = snapshot.child("${userId}").child("correo").getValue().toString(),
                                              rol = snapshot.child("${userId}").child("rol").getValue().toString(),
                                              telegran = snapshot.child("${userId}").child("telefono").getValue().toString(),
                                              direccion = snapshot.child("${userId}").child("direccion").getValue().toString(),
                                              whatsapp = snapshot.child("${userId}").child("telefono").getValue().toString(),
                                              telefono = snapshot.child("${userId}").child("telefono").getValue().toString() )
                }
                 mBinding.tvdpCliente.text = usuario.nombres
                mBinding.tvdpCorreo.text = usuario.correo
                mBinding.tvdpDir.text = usuario.direccion
                mBinding.tvdpTel.text = usuario.telefono

                mBinding.progressBarComprador.visibility = View.GONE

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }


}