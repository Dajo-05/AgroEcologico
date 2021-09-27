package com.example.agroecologico.FragmentosVendedor

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agroecologico.databinding.FragmentHomeVendedorBinding
import com.example.pruebaapp.data.PuestoVentaData
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class HomeVendedorFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeVendedorBinding

    private val RC_GALERY=7
    private val PATH_IMAGENES="iMAGENES"
    private var mImageSeleccionarUri:Uri?=null

    private lateinit var mStoreReference: StorageReference
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var usuario: PuestoVentaData



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth= Firebase.auth
        database=Firebase.database.reference
        mBinding= FragmentHomeVendedorBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.btnSeleccinar.setOnClickListener { abrirGaleria() }
        mStoreReference= FirebaseStorage.getInstance().reference
        leerDato()

        mBinding.BtnModPV.setOnClickListener {
            modificar()}
    }

    private fun modificar() {
        val negocio = mBinding.edtNombrePV.text.toString()


        if (mImageSeleccionarUri != null && negocio.isNotEmpty()){
            val save = mStoreReference.child(usuario.nombrePuesto)
            val asignar = save.child(usuario.nombrePuesto+mImageSeleccionarUri!!.lastPathSegment)
            asignar.putFile(mImageSeleccionarUri!!).addOnSuccessListener {
                asignar.downloadUrl.addOnSuccessListener {
                    val urlImagen = it.toString()
                    usuario.foto = urlImagen
                    usuario.nombrePuesto = negocio
                    database.child("PuestoVenta").child("${usuario.idpuesto}").setValue(usuario)
                }

            }
            Snackbar.make(mBinding.root, "Se Edito foto y nombre del puesto", Snackbar.LENGTH_SHORT).show()

        }else if (mImageSeleccionarUri == null && negocio.isNotEmpty()){
            usuario.nombrePuesto = negocio
            database.child("PuestoVenta").child("${usuario.idpuesto}").setValue(usuario)
            Snackbar.make(mBinding.root, "Se Edito nombre del puesto", Snackbar.LENGTH_SHORT).show()

        }else if (mImageSeleccionarUri != null && !negocio.isNotEmpty()){
            val save = mStoreReference.child(usuario.nombrePuesto)
            val asignar = save.child(usuario.nombrePuesto+mImageSeleccionarUri!!.lastPathSegment)
            asignar.putFile(mImageSeleccionarUri!!).addOnSuccessListener {
                asignar.downloadUrl.addOnSuccessListener {
                    val urlImagen = it.toString()
                    usuario.foto = urlImagen
                    database.child("PuestoVenta").child("${usuario.idpuesto}").setValue(usuario)
                }

            }
            Snackbar.make(mBinding.root, "Se Edito foto del puesto", Snackbar.LENGTH_SHORT).show()

        }else{
            Snackbar.make(mBinding.root, "Por favor seleccione una imagen o escriba el nombre del puesto", Snackbar.LENGTH_SHORT).show()
        }

        //leerDato()
    }

    private fun leerDato() {
        val userId = auth.currentUser?.uid.toString()
        val dbpuesto=FirebaseDatabase.getInstance().reference.child("PuestoVenta")
        val recuperar=object :ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               snapshot.children.forEach {
                   mBinding.MiPV.text=it.child("nombrePuesto").getValue().toString()
                   usuario= PuestoVentaData(idpuesto = it.child("idpuesto").getValue().toString()
                       , nombrePuesto =  it.child("nombrePuesto").getValue().toString(),
                        telefono =  it.child("telefono").getValue().toString(),
                        correo =  it.child("correo").getValue().toString(),
                        whatsapp= it.child("whatsapp").getValue().toString(),
                        telegran =  it.child("telegram").getValue().toString(),
                        foto =  it.child("foto").getValue().toString())
               }
               //Log.d("Datos leidos","${usuario}")
           }

           override fun onCancelled(error: DatabaseError) {
               Log.d("Error",error.toString())
           }

       }
        dbpuesto.addValueEventListener(recuperar)
    }

    private fun abrirGaleria() {
        val intent=Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        Log.d("Ingresar","Abrir la Galeria")
        startActivityForResult(intent,RC_GALERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("fuera del if 1","$resultCode")
        if (resultCode == Activity.RESULT_OK){
            Log.d("dentro del if1 pt1","$resultCode")
            Log.d("dentro del if1 pt2","$RC_GALERY")
            if (requestCode == RC_GALERY){
                mImageSeleccionarUri = data?.data

                Log.d("esto es antes del erro", mImageSeleccionarUri.toString())
                mBinding.ImgTerreno.setImageURI(mImageSeleccionarUri)
            }else{
                Snackbar.make(mBinding.root, "Mensaje2", Snackbar.LENGTH_SHORT).show()
            }
        }else{
            Snackbar.make(mBinding.root, "Mensaje1", Snackbar.LENGTH_SHORT).show()
        }
    }


}



