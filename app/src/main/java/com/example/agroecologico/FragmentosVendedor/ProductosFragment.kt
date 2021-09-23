package com.example.agroecologico.FragmentosVendedor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentProductosBinding
import com.example.agroecologico.fragmento.PvAdminFragment
import com.example.agroecologico.fragmento.UnidadvAdminFragment


class ProductosFragment : Fragment() {


    private lateinit var mBinding: FragmentProductosBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentProductosBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.cardCrearProsucto.setOnClickListener { Acciones(1) }
        mBinding.cardActualizarProsucto.setOnClickListener { Acciones(2) }
    }

    private fun Acciones(i: Int) {
        if (i == 1) {
            val fragmentoAdd = AddProductoFragment()
            val transacion = fragmentManager?.beginTransaction()
            transacion?.replace(R.id.flMain, fragmentoAdd)
                ?.addToBackStack("ProductosFragment")
                ?.commit()
        } else if (i == 2) {

            val fragmentoListado = ListaProductoFragment()
            val transacion = fragmentManager?.beginTransaction()
            transacion?.replace(R.id.flMain, fragmentoListado)
                ?.addToBackStack(null)
                ?.commit()

        }

    }



}