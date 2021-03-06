package com.example.agroecologico

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.agroecologico.FragmentoComprador.RegistroCompradorFragment
import com.example.agroecologico.databinding.ActivityInicioSesionBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlin.math.sign

class InicioSesion: AppCompatActivity() {

    private lateinit var usuario: FirebaseAuth
    private lateinit var vincular: ActivityInicioSesionBinding

    private  lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentoActivo: Fragment

   // private lateinit var googleSignInClient: GoogleSignInClient




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



        vincular.registrar.setOnClickListener {
            registro()
            //Snackbar.make(vincular.root, "La opcion no se encuentra disponible por el momento", Snackbar.LENGTH_SHORT).show()

        }
        // Configure Google Sign In
     /*   val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)*/
        // [END config_signin]

        vincular.GoogleBtn.setOnClickListener {
            //signIn()
            Snackbar.make(vincular.root, "La opcion no se encuentra disponible por el momento", Snackbar.LENGTH_SHORT).show()
        }

        vincular.FaceBookBtn.setOnClickListener {
            Snackbar.make(vincular.root, "La opcion no se encuentra disponible por el momento", Snackbar.LENGTH_SHORT).show()

        }




    }

    // [START onactivityresult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /* Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Error", "Google sign in failed", e)
            }
        }*/
    }

    /* [START signin]
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 9001)
    }
    */

    private fun registro(){
        vincular.linearLayout.visibility = View.GONE
        vincular.linearLayout3.visibility = View.GONE
        val registrar = RegistroCompradorFragment()
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
        val transicion = fragmentManager.beginTransaction()

        transicion.add(R.id.flregistro,registrar)
        transicion.addToBackStack(null)
        transicion.commit()
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