package com.example.agroecologico.FragmentoComprador

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.agroecologico.R
import com.example.agroecologico.adapters.CarritoAdapter
import com.example.agroecologico.adapters.CompraAdapter
import com.example.agroecologico.data.Pedidos
import com.example.agroecologico.data.Producto
import com.example.agroecologico.data.ProductoComprado
import com.example.agroecologico.data.UsuarioData
import com.example.agroecologico.databinding.FragmentCarritoCompraBinding
import com.example.agroecologico.databinding.FragmentListaProductosCompradorBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class CarritoCompraFragment : Fragment(), CarritoAdapter.onItemClikListener {

    private lateinit var mBinding: FragmentCarritoCompraBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var ListaNegocio: MutableList<ProductoComprado>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var adaptador: CarritoAdapter
    private  var itemRV: Int  = 0
    private lateinit var userId: String
    private lateinit var listaIdproductos:  ArrayList<String>
    private lateinit var usuario: UsuarioData
    private lateinit var pedido: Pedidos
    private  lateinit var entrega: String






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth= Firebase.auth
        userId = arguments!!.getString("idPuesto").toString()

        mBinding= FragmentCarritoCompraBinding.inflate(inflater, container, false)
        ListaNegocio = arrayListOf()
        listaIdproductos = arrayListOf()



        cargaDatos()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.btnEnviarpedido.setOnClickListener { EnviarPedido()}
    }


    override fun onItemClik(position: Int) {
        itemRV = position

        val ventana = layoutInflater.inflate(R.layout.dialog_add, null)
        ventana.findViewById<TextView>(R.id.tvNombreProducto).text = ListaNegocio[itemRV].nombreProducto
        ventana.findViewById<TextInputEditText>(R.id.etCantidad1).setText(ListaNegocio[itemRV].cantidad)
        MaterialAlertDialogBuilder(context!!)
            .setView(ventana)
            .setPositiveButton("Agregar Cantida",{dialogInterface, i_ ->

                val canti =  ventana.findViewById<TextInputEditText>(R.id.etCantidad1).text.toString()

                actualizarCantidad(ListaNegocio[itemRV],listaIdproductos[itemRV], canti)
                Log.d("nueva cantidad", "${canti}")


            }).show()
    }

    fun EnviarPedido(){
        mBinding.Carga3.visibility = View.VISIBLE
        val user = auth.currentUser?.uid.toString()
        var total: Int = 0
        entrega = mBinding.etEntrega1.text.toString()
         ListaNegocio.forEach {
             total = total + (it.valtotal).toInt()
         }
        val enviarpedidobd = FirebaseDatabase.getInstance().getReference("Usuarios")
        val query = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    usuario = UsuarioData(nombres = snapshot.child("${user}").child("nombres").getValue().toString(),
                        docuemnto = snapshot.child("${user}").child("documento").getValue().toString(),
                        correo = snapshot.child("${user}").child("correo").getValue().toString(),
                        rol = snapshot.child("${user}").child("rol").getValue().toString(),
                        telegran = snapshot.child("${user}").child("telefono").getValue().toString(),
                        direccion = snapshot.child("${user}").child("direccion").getValue().toString(),
                        whatsapp = snapshot.child("${user}").child("telefono").getValue().toString(),
                        telefono = snapshot.child("${user}").child("telefono").getValue().toString() )
                }

                pedido = Pedidos(cliente = usuario.nombres, direccion = usuario.direccion, telefono = usuario.telefono,
                                    correo = usuario.correo, entrega = entrega, total = total.toString() )

                ListaNegocio.forEach {
                    pedido.compra.add(it)
                }

                database = FirebaseDatabase.getInstance().reference.child("Pedidos")
                database.child("${userId}").push().setValue(pedido)
                mBinding.Carga3.visibility = View.GONE
                Snackbar.make(mBinding.root, "Se envio el Pedido", Snackbar.LENGTH_SHORT).show()


            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error Enviarpedido", "${error}")
            }

        }
        enviarpedidobd.addValueEventListener(query)

    }



    fun actualizarCantidad(compra : ProductoComprado, idpro: String, cantidad: String){
        val user = auth.currentUser?.uid.toString()
        var actualizado = ProductoComprado(nombreProducto = compra.nombreProducto, precio = compra.precio,cantidad = compra.cantidad,
                                            imagProducto = compra.imagProducto,unidad = compra.unidad,valtotal = compra.valtotal )
        var nvalor = (cantidad).toInt() * (compra.precio).toInt()
        actualizado.cantidad = cantidad
        actualizado.valtotal = nvalor.toString()

        database = FirebaseDatabase.getInstance().getReference("CarritoTemporal").child(user)
        database.child("${userId}").child("${idpro}").setValue(actualizado)

      //  Refrescar()
        adaptador.notifyDataSetChanged()



    }

    fun Refrescar(){
        fragmentManager!!
            .beginTransaction()
            .detach(this)
            .attach(this).commit()
    }

    private fun cargaDatos() {
        mBinding.Carga3.visibility = View.VISIBLE
        val user = auth.currentUser?.uid.toString()
        database = FirebaseDatabase.getInstance().getReference("CarritoTemporal").child(user)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for ( data in snapshot.child(userId).children){
                        var ids = data.key.toString()
                        var producto = ProductoComprado( nombreProducto = data.child("nombreProducto").getValue().toString(),
                            precio = data.child("precio").getValue().toString(),
                            cantidad = data.child("cantidad").getValue().toString(),
                            imagProducto = data.child("imagProducto").getValue().toString(),
                            unidad = data.child("unidad").getValue().toString(), valtotal =  data.child("valtotal").getValue().toString() )
                        ListaNegocio.add(producto)
                        listaIdproductos.add(ids)

                    }

                }


                Log.d("Lista Comra", "${ListaNegocio}")
                //Log.d("ids producto", "${ listaIdproductos}")
                adaptador = CarritoAdapter(ListaNegocio, this@CarritoCompraFragment)
                mLayoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)

                mBinding.rvCarrito.apply {
                    layoutManager = mLayoutManager
                    adapter = adaptador
                }
               mBinding.Carga3.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error cargaDatos", "${ error}")
            }

        })
    }


}