package com.example.agroecologico.FragmentosVendedor

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*


class AddProductoFragment : Fragment() {

    // identificar del inten
    private val RC_GALERY = 7
    private val PATH_IMAGENES = "Imagenes"

    private lateinit var mBinding: FragmentAddProductoBinding
    private lateinit var database: DatabaseReference
    private var mImageSeleccionarUri: Uri?=null


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.btnSeleccinar.setOnClickListener { abrirGaleria() }

    }

    private fun abrirGaleria() {
        // creamos el intent para abrir la galeria
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        Log.d("ingresoro","abrior a la galeria")
        startActivityForResult(intent, RC_GALERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("fuerad el if 1","$resultCode")
        if (resultCode == Activity.RESULT_OK){
            Log.d("dentro del if1 pt1","$resultCode")
            Log.d("dentro del if1 pt2","$RC_GALERY")
            if (requestCode == RC_GALERY){
                mImageSeleccionarUri = data?.data
                // var datosImag = mImageSeleccionarUri
                Log.d("esto es antes del erro", mImageSeleccionarUri.toString())
                mBinding.ivPhoto.setImageURI(mImageSeleccionarUri)
            }else{
                Snackbar.make(mBinding.root, "Mensaje2", Snackbar.LENGTH_SHORT).show()
            }
        }else{
            Snackbar.make(mBinding.root, "Mensjae 1", Snackbar.LENGTH_SHORT).show()
        }
    }


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
              Log.d("Elemto select", error.toString())
          }

      }
        query.addValueEventListener(objeto)



    }


}