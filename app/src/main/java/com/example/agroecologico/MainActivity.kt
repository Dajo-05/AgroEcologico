package com.example.agroecologico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.agroecologico.FragmentosVendedor.HomeVendedorFragment
import com.example.agroecologico.FragmentosVendedor.ListaProductoFragment
import com.example.agroecologico.FragmentosVendedor.ProductosFragment
import com.example.agroecologico.databinding.ActivityMainBinding
import com.example.agroecologico.fragmento.HomeAdminFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var usuario: FirebaseAuth
    private lateinit var vincular: ActivityMainBinding
    private  lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentoActivo: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vincular = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vincular.root)
        //Vincula el usuario a Firebase
        usuario = Firebase.auth


        CargarFragmento()

    }
        //Funcion para cerrar la sesion iniciada
        private fun signOut() {
            Firebase.auth.signOut()
            val intent = Intent(this, InicioSesion::class.java)
            startActivity(intent)
        }
    /**
     * metodo que carga los fragmentos d
     * homeVendedor: se crea la instancia home del vendor y es la misma
     * de los datos
     * proVendedor: se crea la instaciona del fragmento de producto
     * @barra_navegacion : metodo del navigation bar, que carga los fragmentos
     * */

    private fun CargarFragmento() {
       /* val homeadminf = HomeAdminFragment()
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
        val transicion = fragmentManager.beginTransaction()
        transicion.add(R.id.flMain,homeadminf)
        transicion.commit()*/
        fragmentManager = supportFragmentManager
        val homeVendedor = HomeVendedorFragment()
        val proVendedor = ProductosFragment()


        fragmentoActivo = homeVendedor

        fragmentManager.beginTransaction()
            .add(R.id.flMain, homeVendedor)
            .commit()

        fragmentManager.beginTransaction()
            .add(R.id.flMain, proVendedor)
            .hide(proVendedor)
            .commit()
        /* @barra_navegacion*/
        vincular.btnNav.setOnNavigationItemReselectedListener { item ->
            when(item.itemId) {
                R.id.home_vendedor -> {
                    fragmentManager.beginTransaction()
                        .hide(fragmentoActivo)
                        .show(homeVendedor)
                        .commit()
                    fragmentoActivo = homeVendedor

                }
                R.id.producto -> {
                    fragmentManager.beginTransaction()
                        .hide(fragmentoActivo)
                        .show(proVendedor)
                        .commit()
                    fragmentoActivo = proVendedor

                }
                R.id.pedidos -> {

                    Snackbar.make(vincular.root, "Opcion no disponible en el momento", Snackbar.LENGTH_SHORT).show()

                }

            }
        }



    }
    /**
     * @param onCreateOptionsMenu
     * pinta el boton del logout
     * **/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)

    }
/**
 *@param onOptionsItemSelected
 * evento click para cerrar sesion
 * **/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when(item.itemId){
             R.id.butonSalir ->{
                 val intent = Intent(this, InicioSesion::class.java)
                 startActivity(intent)
                 signOut()
             }
         }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        
        super.onBackPressed()
        finish()
    }


    }


