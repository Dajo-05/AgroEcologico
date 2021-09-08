package com.example.agroecologico.fragmento

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentUnidadvAdminBinding
import com.example.agroecologico.data.UnidadData
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class UnidadvAdminFragment : Fragment() {

    private lateinit var mBinding: FragmentUnidadvAdminBinding
    private lateinit var database: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentUnidadvAdminBinding.inflate(inflater,container, false)
        database = Firebase.database.reference
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.btnCrearUnidad.setOnClickListener {
            val unidad = UnidadData(nombreUnidad = mBinding.teNombreUnidad1.text.toString(), abrevitura = mBinding.teAnbreviatura1.text.toString())
            if (VerificarDatos(unidad)){
                Acciones(unidad)
            }else{

                Snackbar.make(mBinding.root, "Debe llenar todos los Campos", Snackbar.LENGTH_SHORT).show()

            }


        }
    }
    private fun Acciones(unidadData: UnidadData) {
        database.child("UnidadVenta").child("${unidadData.nombreUnidad}").setValue(unidadData)
        val fragmenthome = HomeAdminFragment()
        val transacion = fragmentManager?.beginTransaction()
        Snackbar.make(mBinding.root, "funcionó el metodo", Snackbar.LENGTH_SHORT).show()
        transacion?.replace(R.id.flMain,fragmenthome)?.commit()

    }

    private fun VerificarDatos(data: UnidadData): Boolean {
           var valor: Boolean = false
        if (data.nombreUnidad.isNotEmpty()  && data.abrevitura.isNotEmpty()){
           valor = true
            return valor
        }else{
            return valor
        }

    }


}