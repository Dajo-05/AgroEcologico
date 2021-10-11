package com.example.agroecologico.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agroecologico.R
import com.example.agroecologico.data.Pedidos
import com.example.agroecologico.data.Producto
import com.example.agroecologico.databinding.PedidoItemBinding
import com.example.agroecologico.databinding.ProductitemBinding

class PedidosAdapter(val pedidosList: MutableList<Pedidos>, val listener: onItemClikListener) : RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder>() {

    private lateinit var mContext: Context
    //private lateinit var mListener : onItemClikListener


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PedidosAdapter.PedidoViewHolder {
        mContext = parent.context
        val items = LayoutInflater.from(parent.context).inflate(R.layout.pedido_item,parent,false)
        return PedidoViewHolder(items)
    }

    override fun onBindViewHolder(holder: PedidosAdapter.PedidoViewHolder, position: Int) {
        val pedidoItem: Pedidos = pedidosList[position]
        holder.Abinding.pCliente.text = pedidoItem.cliente
        holder.Abinding.pTotal.text = pedidoItem.total

    }

    override fun getItemCount(): Int {
        return pedidosList.size
    }

    interface onItemClikListener{
        fun onItemClik(position: Int)
    }


    inner class PedidoViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val Abinding = PedidoItemBinding.bind(view)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                listener.onItemClik(position)
            }
        }

    }
}