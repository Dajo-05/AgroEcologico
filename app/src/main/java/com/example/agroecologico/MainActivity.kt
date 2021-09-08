package com.example.agroecologico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.agroecologico.databinding.ActivityMainBinding
import com.example.agroecologico.fragmento.HomeAdminFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var usuario: FirebaseAuth
    private lateinit var vincular: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vincular = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vincular.root)
        //Vincula el usuario a Firebase
        usuario = Firebase.auth

        //Funcion que permite al dar click en el boton salir a la pantalla loguin
        vincular.backImageView.setOnClickListener {
            val intent = Intent(this, InicioSesion::class.java)
            startActivity(intent)
            signOut()
        }

        CargarFragmento()

    }
        //Funcion para cerrar la sesion iniciada
        private fun signOut() {
            Firebase.auth.signOut()
            val intent = Intent(this, InicioSesion::class.java)
            startActivity(intent)
        }

    private fun CargarFragmento() {
        val homeadminf = HomeAdminFragment()
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
        val transicion = fragmentManager.beginTransaction()
        transicion.add(R.id.flMain,homeadminf)
        transicion.commit()


    }



    }


