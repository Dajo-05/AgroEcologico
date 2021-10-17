package com.example.agroecologico.FragmentoComprador

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agroecologico.FragmentosVendedor.DetallePedidoFragment
import com.example.agroecologico.R
import com.example.agroecologico.adapters.AdapterCompNegocio
import com.example.agroecologico.databinding.FragmentHomeCompradorBinding
import com.example.agroecologico.databinding.FragmentListaNegociosCompradorBinding
import com.example.pruebaapp.data.PuestoVentaData
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class ListaNegociosCompradorFragment : Fragment(), AdapterCompNegocio.onItemClikListener {

    private lateinit var mBinding: FragmentListaNegociosCompradorBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var ListaNegocio: MutableList<PuestoVentaData>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var adaptador: AdapterCompNegocio
    private  var itemRV: Int  = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth= Firebase.auth
        mBinding= FragmentListaNegociosCompradorBinding.inflate(inflater, container, false)
        ListaNegocio = arrayListOf()
        cargaDatos()

        return mBinding.root
    }

    private fun cargaDatos() {
        mBinding.progressBarCompraLista.visibility = View.VISIBLE
       database = FirebaseDatabase.getInstance().reference.child("PuestoVenta")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

               if (snapshot.exists()){

                   for ( data in snapshot.children){
                       var negocio = PuestoVentaData( nombrePuesto = data.child("nombrePuesto").getValue().toString(),
                                                      idpuesto =  data.child("idpuesto").getValue().toString(),
                                                      telefono = data.child("telefono").getValue().toString(),
                                                      correo = data.child("correo").getValue().toString(),
                                                      whatsapp = data.child("whatsapp").getValue().toString(),
                                                      telegran = data.child("telegran").getValue().toString(),
                                                       foto = data.child("foto").getValue().toString(),
                                                       vendedor1 = data.child("vendedor1").getValue().toString(),
                                                      imgVendedor1 = data.child("imgVendedor1").getValue().toString())
                       ListaNegocio.add(negocio)

                   }

               }

                adaptador = AdapterCompNegocio(ListaNegocio, this@ListaNegociosCompradorFragment)
                mLayoutManager = LinearLayoutManager(context)
                mBinding.rvNegocios.apply {
                    layoutManager = mLayoutManager
                    adapter = adaptador

                }
                mBinding.progressBarCompraLista.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onItemClik(position: Int) {
        Log.d("pedido", "${ListaNegocio[position]}")
        itemRV = position
        VerProductos(itemRV)
    }

    fun VerProductos(posicion: Int){
        Snackbar.make(mBinding.root, "Nombre negocio: ${ListaNegocio[posicion].nombrePuesto}", Snackbar.LENGTH_SHORT).show()

        var bundle  = Bundle()
        var dato = "${ListaNegocio[posicion].idpuesto}"
        Log.d("id puestolist", "${dato}")
        bundle.putString("idPuesto","${dato}")
        val fragmentoListado = ListaProductosCompradorFragment()
        val transacion = fragmentManager?.beginTransaction()
        fragmentoListado.arguments = bundle
        transacion?.replace(R.id.flMain, fragmentoListado)
            ?.addToBackStack(null)
            ?.commit()
    }

}