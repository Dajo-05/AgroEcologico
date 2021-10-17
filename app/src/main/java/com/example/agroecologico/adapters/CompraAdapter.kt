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
import com.example.agroecologico.databinding.CardProductoBinding
import com.example.pruebaapp.data.PuestoVentaData


class CompraAdapter (val ProductList: MutableList<Producto>, val listener: CompraAdapter.onItemClikListener)
    : RecyclerView.Adapter<CompraAdapter.CompraViewHolder>() {
    private lateinit var mContext: Context

    interface onItemClikListener{
        fun onItemClik(position: Int)
    }

    inner class CompraViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding  = CardProductoBinding.bind(view)
        init {
            view.setOnClickListener {
                val position = adapterPosition
                listener.onItemClik(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompraViewHolder {
        mContext = parent.context
        val items = LayoutInflater.from(parent.context).inflate(R.layout.card_producto,parent,false)
        return CompraViewHolder(items)
    }

    override fun onBindViewHolder(holder: CompraViewHolder, position: Int) {
        val itemItem: Producto = ProductList[position]

        with(holder){
            binding.productTitle.text = itemItem.nombreProducto
            binding.productPrice.text = itemItem.precio
            Glide.with(mContext)
                .load(itemItem.imagProducto)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.productImage)
        }
    }

    override fun getItemCount(): Int {
      return ProductList.size
    }

}