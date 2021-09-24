package com.example.agroecologico.FragmentosVendedor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.get
import com.example.agroecologico.R
import com.example.agroecologico.data.UnidadData
import com.example.agroecologico.databinding.FragmentAddProductoBinding
import com.google.firebase.database.*


class AddProductoFragment : Fragment() {

    private lateinit var mBinding: FragmentAddProductoBinding
    private lateinit var database: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentAddProductoBinding.inflate(inflater,container, false)
        /*val items = listOf("Material", "Design", "Components", "Android")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        mBinding.atUnidadVenta.setAdapter(adapter)*/


        Datosslect()
        return mBinding.root

    }

   /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }*/

    private fun Datosslect() {
        val query = FirebaseDatabase.getInstance().reference.child("UnidadVenta")
        val unidad = mutableListOf<UnidadData>()
        var items = arrayListOf<String>()

      val objeto = object : ValueEventListener{
          override fun onDataChange(snapshot: DataSnapshot) {
              unidad.clear()
              snapshot.children.forEach {
                  val Uni  = UnidadData(nombreUnidad =  it.child("nombreUnidad").getValue().toString(),
                      abrevitura =it.child("abrevitura").getValue().toString())
                  //Uni?.let { unidad.add(it) }
                  items.add(Uni.nombreUnidad)
                  unidad.add(Uni)
              }

              val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
              mBinding.atUnidadVenta.setAdapter(adapter)


          }

          override fun onCancelled(error: DatabaseError) {
              TODO("Not yet implemented")
          }

      }
        query.addValueEventListener(objeto)



    }


}