package com.example.agroecologico

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.example.agroecologico.databinding.ActivityInicioSesionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class InicioSesion: AppCompatActivity() {

    private lateinit var usuario: FirebaseAuth
    private lateinit var vincular: ActivityInicioSesionBinding
    private lateinit var database: DatabaseReference
    private val DatoRol = "Drol"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vincular = ActivityInicioSesionBinding.inflate(layoutInflater)
        setContentView(vincular.root)
        // Initialize Firebase Auth
        usuario = Firebase.auth
        vincular.signInAppCompatButton.setOnClickListener {
            val correo = vincular.emailEditText.text.toString()
            val contraseña = vincular.passwordEditText.text.toString()

            when {//permite que al dar click con un correo vacio no se cierre la app
                correo.isEmpty() || contraseña.isEmpty()-> {
                    Toast.makeText(baseContext, "Correo o contraseña incorectos.",
                        Toast.LENGTH_SHORT).show()
                }else -> {//si la informacion es correcta inicia sesion
                InicioSesion(correo, contraseña)
                }
            }
        }

    }

    public override fun onStart() {
        super.onStart()
        // Verifica si el usuario existe
        val currentUser = usuario.currentUser
        if(currentUser != null){
            redireccionar();
        }
    }


    //funcion para validar el incio de sesion
    private fun InicioSesion (correo:String, contraseña:String){
        usuario.signInWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Si es correcto el los datos ingresados inicia seccion
                    Log.d("TAG", "signInWithEmail:success")
                    redireccionar()
                } else {
                    //si los datos ingresados son incorectos muestra un mensaje
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Correo o contraseña incorectos.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun redireccionar() {//permite redireccionarse a la pantalla principal
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)


    }

    /*override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    val datoRol: SharedPreferences = getSharedPreferences(DatoRol, Context.MODE_PRIVATE)
        val editar: SharedPreferences.Editor = datoRol.edit()
     database = FirebaseDatabase.getInstance().getReference("Usuarios")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dato = snapshot.child("${userId}").child("rol").getValue().toString()
                Log.d("Rol login","${dato}")
                editar.putString("rol",dato)
                var rol = datoRol.getString("rol","nada")
                Log.d("Rol login pre","${rol}")
                this@InicioSesion.startActivity(intent)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



    */




}