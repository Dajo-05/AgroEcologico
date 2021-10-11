package com.example.agroecologico.FragmentosVendedor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.agroecologico.R
import com.example.agroecologico.data.Pedidos
import com.example.agroecologico.databinding.FragmentDetallePedidoBinding
import com.example.agroecologico.databinding.FragmentListaPedidosBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class DetallePedidoFragment : Fragment() {

    private lateinit var mBinding: FragmentDetallePedidoBinding




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentDetallePedidoBinding.inflate(inflater, container, false)

        return mBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null){
          var  listaDetalle: Pedidos? = arguments!!.getParcelable("pedido")
            mBinding.tvprueba.text = listaDetalle?.cliente
            Log.d("Detalle pedido", "${listaDetalle!!.compra[0].nombreProducto}")

        }
    }


}