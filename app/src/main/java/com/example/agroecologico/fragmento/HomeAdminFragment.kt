package com.example.agroecologico.fragmento

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentHomeAdminBinding

import com.google.android.material.snackbar.Snackbar


class HomeAdminFragment : Fragment() {

    private lateinit var mBindg: FragmentHomeAdminBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBindg = FragmentHomeAdminBinding.inflate(inflater,container,false)
        return mBindg.root




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBindg.cvPuestoAdmin.setOnClickListener { Acciones(1)}
        mBindg.cvAddunidad.setOnClickListener { Acciones(2) }
    }
    private fun Acciones(i: Int) {
            if (i == 1) {
                val fragmentoPV = PvAdminFragment()
                val transacion = fragmentManager?.beginTransaction()
                transacion?.replace(R.id.flMain, fragmentoPV)?.commit()
            }else if (i == 2){

                val fragmentoUv = UnidadvAdminFragment()
                val transacion = fragmentManager?.beginTransaction()
                transacion?.replace(R.id.flMain, fragmentoUv)?.commit()

            }



    }


}