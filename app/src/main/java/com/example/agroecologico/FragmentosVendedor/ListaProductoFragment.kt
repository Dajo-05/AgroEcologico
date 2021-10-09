package com.example.agroecologico.FragmentosVendedor

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.agroecologico.R
import com.example.agroecologico.adapters.ProductoAdapter
import com.example.agroecologico.data.Producto
import com.example.agroecologico.databinding.FragmentListaProductoBinding
import com.example.agroecologico.databinding.ItemProductoBinding
import com.example.agroecologico.databinding.ListaProductoBinding
import com.example.agroecologico.databinding.ProductitemBinding
import com.example.pruebaapp.data.PuestoVentaData
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ListaProductoFragment : Fragment() {

        private lateinit var mBinding: FragmentListaProductoBinding
       // private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Producto,ProductosHolder>
        private lateinit var mLayoutManager: RecyclerView.LayoutManager
        private lateinit var listaDePRoductos: MutableList<Producto>
        private lateinit var AdaptadorProducto: ProductoAdapter
        private lateinit var database: DatabaseReference
        private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentListaProductoBinding.inflate(inflater, container, false)
        database = Firebase.database.reference
        listaDePRoductos = arrayListOf()
        leerDato()



        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


    private fun leerDato() {
        val userId =  Firebase.auth.currentUser?.uid.toString()
        mBinding.progressBar.visibility= View.VISIBLE
        val dbpuesto= FirebaseDatabase.getInstance().reference.child("ProductosVenta").child(userId)
        val recuperar=object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                            val product = it.getValue(Producto::class.java)
                    listaDePRoductos.add(product!!)
                }
                AdaptadorProducto = ProductoAdapter(listaDePRoductos)

                mLayoutManager = LinearLayoutManager(context)

                mBinding.rvproductos.apply {
                    layoutManager = mLayoutManager
                    adapter = AdaptadorProducto
                }
                mBinding.progressBar.visibility= View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error",error.toString())
            }

        }
        dbpuesto.addValueEventListener(recuperar)
    }




}