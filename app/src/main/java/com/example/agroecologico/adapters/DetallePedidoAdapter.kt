package com.example.agroecologico.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agroecologico.R
import com.example.agroecologico.data.Pedidos
import com.example.agroecologico.databinding.ItemDetallePedidoBinding
import com.example.agroecologico.databinding.PedidoItemBinding

class DetallePedidoAdapter(val pedidosList: MutableList<Pedidos>): RecyclerView.Adapter<DetallePedidoAdapter.DetallePedidoViewHolder>() {
    private lateinit var mContext: Context

    inner class DetallePedidoViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val Abinding = ItemDetallePedidoBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetallePedidoViewHolder {
        mContext = parent.context
        val items = LayoutInflater.from(parent.context).inflate(R.layout.pedido_item,parent,false)
        return DetallePedidoViewHolder(items)
    }

    override fun onBindViewHolder(holder: DetallePedidoViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
    return   pedidosList.size
    }

}