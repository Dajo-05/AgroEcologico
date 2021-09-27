package com.example.agroecologico.FragmentosVendedor

import android.content.Context
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
import com.example.agroecologico.data.Producto
import com.example.agroecologico.databinding.FragmentListaProductoBinding
import com.example.agroecologico.databinding.ItemProductoBinding
import com.example.agroecologico.databinding.ListaProductoBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class ListaProductoFragment : Fragment() {

        private lateinit var mBinding: FragmentListaProductoBinding
        private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Producto,ProductosHolder>
        private lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentListaProductoBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = Firebase.auth.currentUser?.uid
        val query = FirebaseDatabase.getInstance()
                                    .reference.child("ProductosVenta")
                                    .child("${user.toString()}")


        val options = FirebaseRecyclerOptions.Builder<Producto>()
                                            .setQuery(query, Producto::class.java)
                                            .build()

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<Producto,ProductosHolder>(options){

             private lateinit var mContext: Context


            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductosHolder {
                 mContext = parent.context
                var view = LayoutInflater.from(mContext).inflate(R.layout.item_producto, parent, false)
                return ProductosHolder(view)
            }

            override fun onBindViewHolder(holder: ProductosHolder, position: Int, model: Producto) {
                val product = getItem(position)

                with(holder){
                    setListenar(product)
                    binding.tvNP.text = product.nombreProducto.toString()
                    binding.tvCan.text =  "Cantidad Disponible: ${product.cantidad} ${product.unidad} "
                    Glide.with(context!!)
                        .load(product.imagProducto)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(binding.imagenp)
                    Log.d("Tag db1", "${product.nombreProducto}")
                    Log.d("Tag db2", "${product.imagProducto}")
                }
            }

            override fun onDataChanged() {
                super.onDataChanged()
                mBinding.progressBar.visibility = View.GONE
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Toast.makeText(mContext,error.message,Toast.LENGTH_SHORT).show()
            }

        }
        mLayoutManager = LinearLayoutManager(context)
        mBinding.rvproductos.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mFirebaseAdapter
        }

      mBinding.rvproductos.itemAnimator = null

    }

    override fun onStart() {
        super.onStart()
        mFirebaseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter.stopListening()
    }


    inner class ProductosHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ListaProductoBinding.bind(view)
        fun setListenar(producto: Producto){
          /*  binding.tvNP.text = producto.nombreProducto.toString()
            binding.tvCan.text =  "Cantidad Disponible: ${producto.cantidad} ${producto.unidad} "
            Glide.with(context!!)
                .load(producto.imagProducto)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.imagenp)*/

        }
    }

}