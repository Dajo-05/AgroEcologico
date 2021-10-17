package com.example.agroecologico

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.agroecologico.FragmentoComprador.CarritoCompraFragment
import com.example.agroecologico.FragmentoComprador.HomeCompradorFragment
import com.example.agroecologico.FragmentoComprador.ListaNegociosCompradorFragment
import com.example.agroecologico.FragmentoComprador.ListadoCompraFragment
import com.example.agroecologico.FragmentosVendedor.HomeVendedorFragment
import com.example.agroecologico.FragmentosVendedor.ListaPedidosFragment
import com.example.agroecologico.FragmentosVendedor.ListaProductoFragment
import com.example.agroecologico.FragmentosVendedor.ProductosFragment
import com.example.agroecologico.databinding.ActivityMainBinding
import com.example.agroecologico.fragmento.HomeAdminFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var usuario: FirebaseAuth
    private lateinit var vincular: ActivityMainBinding
    private  lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentoActivo: Fragment
    private lateinit var database: DatabaseReference

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
        /*val homeadminf = HomeAdminFragment()
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
        val transicion = fragmentManager.beginTransaction()
        transicion.add(R.id.flMain,homeadminf)
        transicion.addToBackStack(null)
        transicion.commit()
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
         vincular.progressBarMain.visibility = View.VISIBLE
        val userId =  usuario.currentUser?.uid.toString()
        //var dato: String
        database = FirebaseDatabase.getInstance().getReference("Usuarios")
        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var dato = snapshot.child("${userId}").child("rol").getValue().toString()

                    if( dato.equals("Admin") ){
                        vincular.btnNav.visibility = View.GONE
                        vincular.progressBarMain.visibility = View.GONE
                        val homeadminf = HomeAdminFragment()
                        val fragmentManager = supportFragmentManager
                        fragmentManager.beginTransaction()
                        val transicion = fragmentManager.beginTransaction()
                        transicion.add(R.id.flMain,homeadminf)
                        transicion.commit()

                    }else if(dato.equals("Vendedor")){
                        vincular.progressBarMain.visibility = View.GONE
                        VistaVendedor()
                    }else if(dato.equals("Comprador")){
                        vincular.progressBarMain.visibility = View.GONE
                        VistaComprador()
                    }
                    vincular.progressBarMain.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



    }



    fun VistaVendedor(){

        fragmentManager = supportFragmentManager
        val homeVendedor = HomeVendedorFragment()
        val proVendedor = ProductosFragment()
        val pedido = ListaPedidosFragment()


        fragmentoActivo = homeVendedor

        fragmentManager.beginTransaction()
            .add(R.id.flMain, homeVendedor)
            .commit()

        fragmentManager.beginTransaction()
            .add(R.id.flMain, proVendedor)
            .hide(proVendedor)
            .commit()

        fragmentManager.beginTransaction()
            .add(R.id.flMain, pedido)
            .hide(pedido)
            .commit()

        /* @barra_navegacion*/
        vincular.btnNav.setOnNavigationItemReselectedListener { item ->
            when(item.itemId) {
                R.id.home_vendedor -> {
                    fragmentManager.beginTransaction()
                        .hide(fragmentoActivo)
                        .show(homeVendedor)
                        .addToBackStack(null)
                        .commit()
                    fragmentoActivo = homeVendedor

                }
                R.id.producto -> {
                    fragmentManager.beginTransaction()
                        .hide(fragmentoActivo)
                        .show(proVendedor)
                        .addToBackStack(null)
                        .commit()
                    fragmentoActivo = proVendedor

                }
                R.id.pedidos -> {
                    fragmentManager.beginTransaction()
                        .hide(fragmentoActivo)
                        .show(pedido)
                        .addToBackStack(null)
                        .commit()
                    fragmentoActivo = pedido

                    // Snackbar.make(vincular.root, "Opcion no disponible en el momento", Snackbar.LENGTH_SHORT).show()

                }

            }
        }

    }


    fun VistaComprador(){

        fragmentManager = supportFragmentManager
        val homeComprador = HomeCompradorFragment()
        val lNegocio = ListaNegociosCompradorFragment()
        val carrito = ListadoCompraFragment()


        fragmentoActivo = homeComprador

        fragmentManager.beginTransaction()
            .add(R.id.flMain, homeComprador)
            .commit()

        fragmentManager.beginTransaction()
            .add(R.id.flMain, lNegocio)
            .hide(lNegocio)
            .commit()

        fragmentManager.beginTransaction()
            .add(R.id.flMain, carrito)
            .hide(carrito)
            .commit()

        /* @barra_navegacion*/
        vincular.btnNav.setOnNavigationItemReselectedListener { item ->
            when(item.itemId) {
                R.id.home_vendedor -> {
                    fragmentManager.beginTransaction()
                        .hide(fragmentoActivo)
                        .show(homeComprador)
                        .commit()
                    fragmentoActivo = homeComprador

                }
                R.id.producto -> {
                    fragmentManager.beginTransaction()
                        .hide(fragmentoActivo)
                        .show(lNegocio)
                        .commit()
                    fragmentoActivo = lNegocio

                }
                R.id.pedidos -> {
                    fragmentManager.beginTransaction()
                        .hide(fragmentoActivo)
                        .show(carrito)
                        .commit()
                    fragmentoActivo = carrito

                    // Snackbar.make(vincular.root, "Opcion no disponible en el momento", Snackbar.LENGTH_SHORT).show()

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


