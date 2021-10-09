package com.example.agroecologico.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.agroecologico.R
import com.example.agroecologico.data.Producto
import com.example.agroecologico.databinding.ProductitemBinding

class ProductoAdapter(val productoList: MutableList<Producto>) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {
    private lateinit var mContext: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductoAdapter.ProductoViewHolder {
        mContext = parent.context
         val Items = LayoutInflater.from(parent.context).inflate(R.layout.productitem,parent,false)
        return ProductoViewHolder(Items)
    }

    override fun onBindViewHolder(holder: ProductoAdapter.ProductoViewHolder, position: Int) {
       val producto : Producto = productoList[position]
         with(holder){
             binding.txtnombreProduct.text = producto.nombreProducto
             binding.txtCantidad.text = "Cantidad disponible: ${producto.cantidad}  ${producto.unidad}"
             Glide.with(mContext)
                 .load(producto.imagProducto)
                 .diskCacheStrategy(DiskCacheStrategy.ALL)
                 .centerCrop()
                 .into(binding.imagenProducto)
         }
    }

    override fun getItemCount(): Int {
      return productoList.size
    }

    inner class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view){
             val binding  = ProductitemBinding.bind(view)

    }

}