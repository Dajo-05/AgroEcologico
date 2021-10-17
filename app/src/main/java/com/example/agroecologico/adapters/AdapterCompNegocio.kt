package com.example.agroecologico.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.agroecologico.R
import com.example.agroecologico.data.Pedidos
import com.example.agroecologico.databinding.ItemProductoBinding
import com.example.agroecologico.databinding.PedidoItemBinding
import com.example.pruebaapp.data.PuestoVentaData

class AdapterCompNegocio (val NegocioList: MutableList<PuestoVentaData>, val listener: AdapterCompNegocio.onItemClikListener)
    : RecyclerView.Adapter<AdapterCompNegocio.NegocioViewHolder>() {

    private lateinit var mContext: Context

    interface onItemClikListener{
        fun onItemClik(position: Int)
    }

    inner class NegocioViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val Abinding = ItemProductoBinding.bind(view)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                listener.onItemClik(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NegocioViewHolder {
        mContext = parent.context
        val items = LayoutInflater.from(parent.context).inflate(R.layout.item_producto,parent,false)
        return NegocioViewHolder(items)

    }

    override fun onBindViewHolder(holder: NegocioViewHolder, position: Int) {
        val NegocioItem: PuestoVentaData = NegocioList[position]

        with(holder){
            Abinding.tvNomProducto.text = NegocioItem.nombrePuesto
            Abinding.tvCantidad.text = NegocioItem.correo
            Glide.with(mContext)
                .load(NegocioItem.foto)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(Abinding.ivproducto)
        }
    }

    override fun getItemCount(): Int {
        return NegocioList.size
    }

}