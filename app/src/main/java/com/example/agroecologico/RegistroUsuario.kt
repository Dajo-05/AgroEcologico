package com.example.agroecologico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.agroecologico.databinding.ActivityInicioSesionBinding
import com.example.agroecologico.databinding.ActivityRegistroUsuarioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.perfmark.Tag
import java.util.regex.Pattern;

class RegistroUsuario : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegistroUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.signUpButton.setOnClickListener {
            val Correo = binding.emailEditText.text.toString()
            val Contaseña = binding.passwordEditText.text.toString()
            val confirmarContraseña = binding.repeatPasswordEditText.text.toString()
            val passwordRegex = Pattern.compile("^" +
                        "(?=.*[@#$%^&+=])" +     //valida almenos un caracer especial
                        ".{6,}" +               //valida que ena almenos 6 caracteres
                        "$",
            )

            if (Correo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(Correo).matches()){
                Toast.makeText(baseContext, "Ingrese un correo valido.",
                    Toast.LENGTH_SHORT).show()
            }else if (Contaseña.isEmpty() || !passwordRegex.matcher(Contaseña).matches()){
                Toast.makeText(baseContext, "La conraseña no muy segura.",
                    Toast.LENGTH_SHORT).show()
            }else if (Contaseña !=confirmarContraseña){
                Toast.makeText(baseContext, "Confirme la conraseña.",
                    Toast.LENGTH_SHORT).show()
            }else {
                crearUsuario(Correo, Contaseña)
            }
        }
        /*binding.backImageView.setOnClickListener {
            val intent = Intent(this, InicioSesion::class.java)
            startActivity(intent)
        }*/
    }
    private fun crearUsuario(correo : String, contraseña : String){
        auth.createUserWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Usuario Creado saisfacoriamene.",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}