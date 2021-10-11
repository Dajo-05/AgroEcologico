package com.example.agroecologico.FragmentosVendedor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agroecologico.R
import com.example.agroecologico.adapters.DetallePedidoAdapter
import com.example.agroecologico.adapters.ProductoAdapter
import com.example.agroecologico.data.Pedidos
import com.example.agroecologico.data.Producto
import com.example.agroecologico.data.ProductoComprado
import com.example.agroecologico.databinding.FragmentDetallePedidoBinding
import com.example.agroecologico.databinding.FragmentListaPedidosBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class DetallePedidoFragment : Fragment() {

    private lateinit var mBinding: FragmentDetallePedidoBinding
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var listaDetalle: MutableList<ProductoComprado>
    private lateinit var AdaptadorDetalle: DetallePedidoAdapter




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentDetallePedidoBinding.inflate(inflater, container, false)
        listaDetalle = arrayListOf()

        return mBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.btExcel.setOnClickListener {
            Snackbar.make(mBinding.root, "Opcion no disponible por el momento", Snackbar.LENGTH_SHORT).show()
        }
        if (arguments != null){
          var  listaDet: Pedidos? = arguments!!.getParcelable("pedido")
            for (deta in listaDet!!.compra){
                var comprados = ProductoComprado(nombreProducto = deta.nombreProducto,
                                                 precio = deta.precio, cantidad = deta.cantidad,imagProducto = deta.imagProducto,
                                                    unidad = deta.unidad, valtotal = deta.valtotal)
                listaDetalle.add(comprados)

            }

            mBinding.tvClienteoDetalle.text = listaDet.cliente
            mBinding.tvCoreoDetalle.text = listaDet.correo
            mBinding.tvDirDetalle.text = listaDet.direccion
            mBinding.tvEntregaDetalle.text = listaDet.entrega
            mBinding.tvtelDetalle.text = listaDet.telefono
            mBinding.tvTotalDetalle.text = listaDet.total

            AdaptadorDetalle = DetallePedidoAdapter(listaDetalle)
            mLayoutManager = LinearLayoutManager(context)
            mBinding.rvDetallePedido.apply {
                layoutManager = mLayoutManager
                adapter = AdaptadorDetalle
            }



        }
    }


}