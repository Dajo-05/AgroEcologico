package com.example.agroecologico.FragmentoComprador

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agroecologico.R
import com.example.agroecologico.adapters.AdapterCompNegocio
import com.example.agroecologico.adapters.CompraAdapter
import com.example.agroecologico.data.Producto
import com.example.agroecologico.data.ProductoComprado
import com.example.agroecologico.databinding.FragmentListaNegociosCompradorBinding
import com.example.agroecologico.databinding.FragmentListaProductosCompradorBinding
import com.example.pruebaapp.data.PuestoVentaData
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class ListaProductosCompradorFragment : Fragment(), CompraAdapter.onItemClikListener {

    private lateinit var mBinding: FragmentListaProductosCompradorBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var ListaNegocio: MutableList<Producto>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var adaptador: CompraAdapter
    private  var itemRV: Int  = 0
    private lateinit var userId: String
    private lateinit var listaIdproductos:  ArrayList<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth= Firebase.auth
        userId = arguments!!.getString("idPuesto").toString()
        Log.d("Id puesto", "${userId}")
        mBinding= FragmentListaProductosCompradorBinding.inflate(inflater, container, false)
        ListaNegocio = arrayListOf()
        listaIdproductos = arrayListOf()
        cargaDatos()

        return mBinding.root
    }

    override fun onItemClik(position: Int) {
        itemRV = position

        enviarCarrito(ListaNegocio[position], listaIdproductos[itemRV] )
    }


    fun enviarCarrito(producto: Producto,idpro: String ){
        val user = auth.currentUser?.uid.toString()
        database = FirebaseDatabase.getInstance().getReference("CarritoTemporal").child(user)
        val mas = 1
        Log.d("cantidad", "${mas}")
        var compra = ProductoComprado(nombreProducto = producto.nombreProducto, precio = producto.precio, cantidad =  mas.toString(),
                        imagProducto = producto.imagProducto,unidad = producto.unidad, valtotal =( mas * (producto.precio).toInt()).toString() )

                    database.child("${userId}").child("${idpro}").setValue(compra)
                    Snackbar.make(mBinding.root, "Se Agregó al carrito: ${producto.nombreProducto}", Snackbar.LENGTH_SHORT).show()

       /* Snackbar.make(mBinding.root, "Cantidad Producto: ${mas++}", Snackbar.LENGTH_SHORT).show()
        var compra = ProductoComprado(nombreProducto = producto.nombreProducto,
                                       precio = producto.precio, cantidad =  mas.toString(),
                                        imagProducto = producto.imagProducto,unidad = producto.unidad,
                                         valtotal =( mas * (producto.precio).toInt()).toString() )

        database.child("${user}").child("${userId}").child("${idpro}").setValue(compra)*/


    }

    private fun cargaDatos() {
        mBinding.CargaPRoducto.visibility = View.VISIBLE
        database = FirebaseDatabase.getInstance().getReference("ProductosVenta")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for ( data in snapshot.child(userId).children){
                        var ids = data.key.toString()
                        var producto = Producto( nombreProducto = data.child("nombreProducto").getValue().toString(),
                                                precio = data.child("precio").getValue().toString(),
                                                cantidad = data.child("cantidad").getValue().toString(),
                                               imagProducto = data.child("imagProducto").getValue().toString(),
                                               unidad = data.child("unidad").getValue().toString() )
                        ListaNegocio.add(producto)
                        listaIdproductos.add(ids)

                    }

                }
                Log.d("Lista Comra", "${ListaNegocio}")
                Log.d("ids producto", "${ listaIdproductos}")
                adaptador = CompraAdapter(ListaNegocio, this@ListaProductosCompradorFragment)
                mLayoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
                mBinding.rvProductoComprador.apply {
                    layoutManager = mLayoutManager
                    adapter = adaptador
                }
                mBinding.CargaPRoducto.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}