package com.example.agroecologico.fragmento

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.agroecologico.ManejadorCorreo
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentPvAdminBinding
import com.example.agroecologico.data.UsuarioData
import com.example.pruebaapp.data.PuestoVentaData
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class PvAdminFragment : Fragment() {

    private lateinit var mBinding: FragmentPvAdminBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         mBinding = FragmentPvAdminBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        database = Firebase.database.reference
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


            mBinding.btnGuardarNegocio.setOnClickListener {
                var puesto = mBinding.tepuesto1.text.toString()
                val user = UsuarioData(nombres = mBinding.tenombre1.text.toString(),docuemnto = mBinding.tedocumento1.text.toString(),
                    correo = mBinding.teEmail1.text.toString(),telefono =  mBinding.teTelefono1.text.toString(),
                    whatsapp = mBinding.teTelefono1.text.toString(), telegran = mBinding.teTelefono1.text.toString())

                var permiso = VerificarDatos(user, puesto)
                if (permiso){
                Acciones(user,puesto)
                }else{
                    Snackbar.make(mBinding.root, "Debe llenar todos los Campos", Snackbar.LENGTH_SHORT).show()
                }
            }


    }

    private fun VerificarDatos(user: UsuarioData, puesto: String): Boolean {
        Log.d("verifi antes del if"," dato${user} ")
        Log.d("verifi antes del if"," dato${puesto} ")
        if (user.nombres.isNotEmpty()  && user.docuemnto.isNotEmpty() && user.correo.isNotEmpty() && user.telefono.isNotEmpty() && puesto.isNotEmpty()){
            Log.d("verificacion"," dato${user} ")
            return true
        }else{
            return false
        }

    }

    private fun Acciones(user: UsuarioData, puesto: String) {

        Registar(user,puesto)
        val fragmenthome = HomeAdminFragment()
        val transacion = fragmentManager?.beginTransaction()
        Snackbar.make(mBinding.root, "funcionó el metodo", Snackbar.LENGTH_SHORT).show()
        transacion?.replace(R.id.flMain,fragmenthome)?.commit()


    }

    private fun Registar(user:UsuarioData, puesto: String){

        Log.d("nombre puesto","${puesto}")
        auth.createUserWithEmailAndPassword(
            mBinding.teEmail1.text.toString(),
            mBinding.tePassword1.text.toString()
        ).addOnCompleteListener(
            { task ->

            if (task.isSuccessful) {

                val userId = auth.currentUser?.uid.toString()
                val venta =  PuestoVentaData(idpuesto = userId, nombrePuesto = puesto, telefono = user.telefono,
                                              correo = user.correo, whatsapp = user.telefono, telegran = user.telefono)
                database.child("Usuarios").child(userId).setValue(user)
                database.child("PuestoVenta").child("${userId}").child(puesto).setValue(venta)

            } else {

                Snackbar.make(
                    mBinding.root,
                    "hubo un problema no se puedo registrar",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        val manejadorCorreo = ManejadorCorreo("${mBinding.teEmail1.text.toString()}",
            "Confirmacion de creación del puesto de venta: ${puesto} en AgroEcologico",
            "Bienvenido a AgroEcologico \n Su Usuarion es: ${ mBinding.teEmail1.text.toString()} \n " +
                    "Su contraseña es: ${mBinding.tePassword1.text.toString()}"+"\n Uselos para iniciar sesion en nuestra aplicacion",
            context)
        manejadorCorreo.enviarEmail()



    }



}