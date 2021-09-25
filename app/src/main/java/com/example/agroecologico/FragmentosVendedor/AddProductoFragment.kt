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


class AddProductoFragment : Fragment(), AdapterView.OnItemClickListener {

    // identificar del inten
    private val RC_GALERY = 7
    private val PATH_IMAGENES = "Imagenes"

    private lateinit var mBinding: FragmentAddProductoBinding
    private lateinit var database: DatabaseReference
    // esta para obtener la direcion de la imagen
    private var mImageSeleccionarUri: Uri?=null
    private lateinit var ite: ArrayAdapter<String>
    private lateinit var unidaselect: String


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
        mBinding.btnProducto.setOnClickListener { GuardarDatos() }

    }

    private fun GuardarDatos() {


        Snackbar.make(mBinding.root, "Metodo de Guardar datos esta unidad ${unidaselect}", Snackbar.LENGTH_SHORT).show()

    }
// para abrir la galeria
    private fun abrirGaleria() {
        // creamos el intent para abrir la galeria
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        Log.d("ingresoro","abrior a la galeria")
        startActivityForResult(intent, RC_GALERY)
    }
// cargar la imagen el en img view
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

              ite = ArrayAdapter(requireContext(), R.layout.list_item, items)
              with(mBinding.atUnidadVenta){
                  setAdapter(ite)
                  onItemClickListener = this@AddProductoFragment
              }




          }

          override fun onCancelled(error: DatabaseError) {
              Log.d("Elemto select", error.toString())
          }

      }
        query.addValueEventListener(objeto)



    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
     val item = parent?.getItemAtPosition(position).toString()
        unidaselect = item.toString()
        //Snackbar.make(mBinding.root, "Desplegable ${item.toString()}", Snackbar.LENGTH_SHORT).show()
    }


}