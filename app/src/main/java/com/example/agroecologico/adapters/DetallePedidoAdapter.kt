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
import com.example.agroecologico.data.ProductoComprado
import com.example.agroecologico.databinding.ItemDetallePedidoBinding
import com.example.agroecologico.databinding.PedidoItemBinding

class DetallePedidoAdapter(val detalleList: MutableList<ProductoComprado>): RecyclerView.Adapter<DetallePedidoAdapter.DetallePedidoViewHolder>() {
    private lateinit var mContext: Context

    inner class DetallePedidoViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val Abinding = ItemDetallePedidoBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetallePedidoViewHolder {
        mContext = parent.context
        val items = LayoutInflater.from(parent.context).inflate(R.layout.item_detalle_pedido,parent,false)
        return DetallePedidoViewHolder(items)
    }

    override fun onBindViewHolder(holder: DetallePedidoViewHolder, position: Int) {
        val detalle : ProductoComprado = detalleList[position]
        with(holder){
            Abinding.tvNombrePedidoP.text = detalle.nombreProducto
            Abinding.tvtCantidadPedido.text = "${detalle.cantidad} ${detalle.unidad}"
            Abinding.tvPreciodetalle.text = "Precio Unitario: $ ${detalle.precio}"
            Abinding.tvtotalPe.text = "Total Compra $ ${detalle.valtotal}"
            Glide.with(mContext)
                .load(detalle.imagProducto)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(Abinding.ivDetallePedido)

        }
    }

    override fun getItemCount(): Int {
    return   detalleList.size
    }

}