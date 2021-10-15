package com.example.agroecologico.FragmentosVendedor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agroecologico.R
import com.example.agroecologico.adapters.PedidosAdapter
import com.example.agroecologico.adapters.ProductoAdapter
import com.example.agroecologico.data.Pedidos
import com.example.agroecologico.data.Producto
import com.example.agroecologico.data.ProductoComprado
import com.example.agroecologico.databinding.FragmentListaPedidosBinding
import com.example.agroecologico.databinding.FragmentListaProductoBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ListaPedidosFragment : Fragment(),PedidosAdapter.onItemClikListener  {

    private lateinit var mBinding: FragmentListaPedidosBinding
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var database: DatabaseReference
   // private lateinit var auth: FirebaseAuth
    private lateinit var adaptador: PedidosAdapter
    private lateinit var listaDePedidos: MutableList<Pedidos>
    private  var itemRV: Int  = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentListaPedidosBinding.inflate(inflater, container, false)
        database = Firebase.database.reference
        listaDePedidos = arrayListOf()
        Log.d("Inico", "ingreso al Fregamento de lista de pedido")
        leerDato()

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun leerDato() {
       // val userId = Firebase.auth.currentUser?.uid.toString()
        Log.d("Metodo", "leer Datos")

        val datos = FirebaseDatabase.getInstance().reference.child("Pedidos")
        val recuperar = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    Log.d("ingreso a pedido", "ingreso al if")
                    for (ds in snapshot.children){
                        var pedido = Pedidos(cliente = ds.child("Cliente").getValue().toString(),
                            correo = ds.child("correo").getValue().toString(),
                            direccion = ds.child("direccion").getValue().toString(),
                            entrega = ds.child("entrega").getValue().toString(),
                            telefono = ds.child("telefono").getValue().toString(),
                             total = ds.child("total").getValue().toString() )

                        for (compra in ds.child("comprado").children){
                             var comp = ProductoComprado(nombreProducto = compra.child("nombreProducto").getValue().toString(),
                                                         precio = compra.child("precio").getValue().toString(),
                                                         cantidad = compra.child("cantidad").getValue().toString(),
                                                         imagProducto = compra.child("imagProducto").getValue().toString(),
                                                         unidad = compra.child("unidad").getValue().toString(),
                                                         valtotal = compra.child("valtotal").getValue().toString())
                            pedido.compra.add(comp)

                        }
                       listaDePedidos.add(pedido)
                    }

                }

                adaptador = PedidosAdapter(listaDePedidos,this@ListaPedidosFragment)

                mLayoutManager = LinearLayoutManager(context)

                mBinding.rvPedido.apply {
                    layoutManager = mLayoutManager
                    adapter = adaptador

                }
                       Log.d("pedido", "${listaDePedidos}")

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error pedido", "${error}")
            }


        }
        datos.addValueEventListener(recuperar)

    }

    override fun onItemClik(position: Int) {
        Log.d("pedido", "${listaDePedidos[position]}")
         itemRV = position
        VerDetalle(itemRV)
    }

    fun VerDetalle(posicion: Int){
        var bundle  = Bundle()
       bundle.putParcelable("pedido", listaDePedidos[posicion])
        val fragmentoListado = DetallePedidoFragment()
        val transacion = fragmentManager?.beginTransaction()
        fragmentoListado.arguments = bundle
        transacion?.replace(R.id.flMain, fragmentoListado)
            ?.addToBackStack(null)
            ?.commit()

    }

}