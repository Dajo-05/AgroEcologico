package com.example.agroecologico.FragmentoComprador

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agroecologico.InicioSesion
import com.example.agroecologico.data.UsuarioData
import com.example.agroecologico.databinding.FragmentRegistroCompradorBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class RegistroCompradorFragment : Fragment() {

    private lateinit var mBinding: FragmentRegistroCompradorBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentRegistroCompradorBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        database = Firebase.database.reference
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.btnRegistrar.setOnClickListener {
            val user= UsuarioData(nombres = mBinding.nombreComprador1.text.toString(),
                docuemnto = mBinding.idComprador1.text.toString(),
                correo = mBinding.emailComprador1.text.toString(),
                rol = "Comprador",
                telefono = mBinding.telComprador1.text.toString(),
                direccion = mBinding.dirComprador1.text.toString(),
                whatsapp = mBinding.telComprador1.text.toString(),
                telegran = mBinding.telComprador1.text.toString()
            )
            var confirmar = confirmarInformacion(user)
            if (confirmar){
                Accion(user)
            }else{
                Snackbar.make(mBinding.root, "Debe llenar todos los Campos", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun confirmarInformacion(user: UsuarioData): Boolean{
        Log.d("Verificar antes"," info${user} ")
        if (user.nombres.isNotEmpty() && user.docuemnto.isNotEmpty() &&
            user.correo.isNotEmpty() && user.telefono.isNotEmpty() && user.direccion.isNotEmpty()){
            Log.d("verificar"," info${user} ")
            return true
        }else{
            return false
        }
    }

    private fun Accion(user: UsuarioData) {

        registro(user)
        Snackbar.make(mBinding.root, "Registro exitoso", Snackbar.LENGTH_SHORT).show()
        devolver()
    }

    private fun registro(user: UsuarioData){
        auth.createUserWithEmailAndPassword(
            mBinding.emailComprador1.text.toString(),
            mBinding.passwordComprador1.text.toString()
        ).addOnCompleteListener(
            { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid.toString()
                database.child("Usuarios").child(userId).setValue(user)
            } else {
                Snackbar.make(
                    mBinding.root,
                    "No se puedo realizar el registro",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun devolver(){//permite regresar al loguin
        val intent = Intent(activity, InicioSesion::class.java)
        activity!!.startActivity(intent)
    }
}