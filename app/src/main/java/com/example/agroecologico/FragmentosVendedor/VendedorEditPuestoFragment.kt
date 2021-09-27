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
import com.example.agroecologico.databinding.FragmentVendedorEditPuestoBinding
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.agroecologico.R
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


class VendedorEditPuestoFragment : Fragment() {

    private val RC_GALERY=7
    private val PATH_IMAGENES="iMAGENES"
    private var mImageSeleccionarUri: Uri?=null
    private var mImageSeleccionarVendedor: Uri?=null

    private lateinit var mStoreReference: StorageReference
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var usuario: PuestoVentaData
    private  var open1: Int = 0
    private  var open2: Int = 0

    private lateinit var mBinding: FragmentVendedorEditPuestoBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth= Firebase.auth
        database= Firebase.database.reference
        mBinding= FragmentVendedorEditPuestoBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.btnSeleccinar.setOnClickListener { abrirGaleria() }
        mBinding.btnSeleccinar2.setOnClickListener{abrirGaleria2()}
        mStoreReference= FirebaseStorage.getInstance().reference
        leerDato()
        mBinding.BtnModPV.setOnClickListener { guardar() }
    }

    private fun leerDato() {
        val userId = auth.currentUser?.uid.toString()
        mBinding.progressBar2.visibility= View.VISIBLE
        val dbpuesto= FirebaseDatabase.getInstance().reference.child("PuestoVenta")
        val recuperar=object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {

                    usuario= PuestoVentaData(
                        idpuesto = it.child("idpuesto").getValue().toString(),
                        nombrePuesto =  it.child("nombrePuesto").getValue().toString(),
                        telefono =  it.child("telefono").getValue().toString(),
                        correo =  it.child("correo").getValue().toString(),
                        whatsapp= it.child("whatsapp").getValue().toString(),
                        telegran =  it.child("telegram").getValue().toString(),
                        foto =  it.child("foto").getValue().toString(),
                        vendedor1 = it.child("vendedor1").getValue().toString(),
                        imgVendedor1 = it.child("imgVendedor1").getValue().toString())


                }
                mBinding.teNomPuesto.setText(usuario.nombrePuesto)
                mBinding.tevendedor.setText(usuario.vendedor1)
                mBinding.progressBar2.visibility= View.GONE
                Glide.with(this@VendedorEditPuestoFragment)
                    .load("${usuario.foto}")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(mBinding.ImgTerreno)
                Glide.with(context!!)
                    .load(usuario.imgVendedor1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(mBinding.ImgVendedro)
                //Log.d("Datos leidos","${usuario}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error",error.toString())
            }

        }
        dbpuesto.addValueEventListener(recuperar)
    }

    private fun guardar(){
        val nomVen = mBinding.tevendedor.text.toString()
        val negocio = mBinding.teNomPuesto.text.toString()
             Log.d("ingreso guardar", "${nomVen} , ${ negocio}")
        if (mImageSeleccionarUri != null && negocio.isNotEmpty() && mImageSeleccionarVendedor != null && nomVen.isNotEmpty()){
            val save = mStoreReference.child(usuario.nombrePuesto)
            val asignar = save.child(usuario.nombrePuesto+mImageSeleccionarUri!!.lastPathSegment)
            val asignar2 = save.child(usuario.nombrePuesto+mImageSeleccionarVendedor!!.lastPathSegment)

            asignar.putFile(mImageSeleccionarUri!!).addOnSuccessListener {
                asignar.downloadUrl.addOnSuccessListener {
                    val urlImagen = it.toString()
                    usuario.foto = urlImagen
                    usuario.nombrePuesto = negocio
                    usuario.vendedor1=nomVen
                    database.child("PuestoVenta").child("${usuario.idpuesto}").setValue(usuario)
                }


            }



            asignar2.putFile(mImageSeleccionarVendedor!!).addOnSuccessListener {
                asignar2.downloadUrl.addOnSuccessListener {
                    val urlImagen = it.toString()
                    usuario.imgVendedor1 = urlImagen
                    database.child("PuestoVenta").child("${usuario.idpuesto}").setValue(usuario)
                }

            }
            Snackbar.make(mBinding.root, "Se Actualió conExito", Snackbar.LENGTH_SHORT).show()
            val fragmentoAdd = HomeVendedorFragment()
            val transacion = fragmentManager?.beginTransaction()
            transacion?.replace(R.id.flMain, fragmentoAdd)
                ?.addToBackStack(null)
                ?.commit()

        }else if (mImageSeleccionarUri == null && negocio.isNotEmpty() && mImageSeleccionarVendedor == null && nomVen.isNotEmpty()){
            usuario.nombrePuesto = negocio
            usuario.vendedor1 = nomVen
            database.child("PuestoVenta").child("${usuario.idpuesto}").setValue(usuario)
            Snackbar.make(mBinding.root, "Se Actualió conExito", Snackbar.LENGTH_SHORT).show()
            val fragmentoAdd = HomeVendedorFragment()
            val transacion = fragmentManager?.beginTransaction()
            transacion?.replace(R.id.flMain, fragmentoAdd)
                ?.addToBackStack(null)
                ?.commit()

        }else if (mImageSeleccionarUri == null && negocio.isNotEmpty() && mImageSeleccionarVendedor != null && nomVen.isNotEmpty()){
            val save = mStoreReference.child(usuario.nombrePuesto)
            val asignar2 = save.child(usuario.nombrePuesto+mImageSeleccionarVendedor!!.lastPathSegment)
            asignar2.putFile(mImageSeleccionarVendedor!!).addOnSuccessListener {
                asignar2.downloadUrl.addOnSuccessListener {
                    val urlImagen = it.toString()
                    usuario.foto = urlImagen
                    usuario.nombrePuesto = negocio
                    usuario.vendedor1=nomVen
                    database.child("PuestoVenta").child("${usuario.idpuesto}").setValue(usuario)
                }

            }
            Snackbar.make(mBinding.root, "Se Actualió conExito", Snackbar.LENGTH_SHORT).show()
            val fragmentoAdd = HomeVendedorFragment()
            val transacion = fragmentManager?.beginTransaction()
            transacion?.replace(R.id.flMain, fragmentoAdd)
                ?.addToBackStack(null)
                ?.commit()

        }else if (mImageSeleccionarUri != null && negocio.isNotEmpty() && mImageSeleccionarVendedor == null && nomVen.isNotEmpty()){
            val save = mStoreReference.child(usuario.nombrePuesto)
            val asignar = save.child(usuario.nombrePuesto+mImageSeleccionarUri!!.lastPathSegment)
            asignar.putFile(mImageSeleccionarUri!!).addOnSuccessListener {
                asignar.downloadUrl.addOnSuccessListener {
                    val urlImagen = it.toString()
                    usuario.foto = urlImagen
                    usuario.nombrePuesto = negocio
                    usuario.vendedor1=nomVen
                    database.child("PuestoVenta").child("${usuario.idpuesto}").setValue(usuario)
                }

            }
            Snackbar.make(mBinding.root, "Se Actualió conExito", Snackbar.LENGTH_SHORT).show()
            val fragmentoAdd = HomeVendedorFragment()
            val transacion = fragmentManager?.beginTransaction()
            transacion?.replace(R.id.flMain, fragmentoAdd)
                ?.addToBackStack(null)
                ?.commit()

        }
        else{
            Snackbar.make(mBinding.root, "Por favor Diligencie los campos", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun abrirGaleria() {
        open1 = 1
        open2 = 0
        val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        Log.d("Ingresar","Abrir la Galeria")
        startActivityForResult(intent,RC_GALERY)
    }
    private fun abrirGaleria2() {
        open2 = 1
        open1 = 0
        val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        Log.d("Ingresar","Abrir la Galeria")
        startActivityForResult(intent,RC_GALERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("fuera del if 1","$resultCode")
        if (resultCode == Activity.RESULT_OK && open1 > 0){
            Log.d("dentro del if1 pt1","$resultCode")
            Log.d("dentro del if1 pt2","$RC_GALERY")
            if (requestCode == RC_GALERY){
                mImageSeleccionarUri = data?.data

                Log.d("esto es antes del erro", mImageSeleccionarUri.toString())
                mBinding.ImgTerreno.setImageURI(mImageSeleccionarUri)
            }else{
                Snackbar.make(mBinding.root, "Mensaje2", Snackbar.LENGTH_SHORT).show()
            }
        }else if (resultCode == Activity.RESULT_OK && open2 > 0){
            Log.d("dentro del if1 pt1","$resultCode")
            Log.d("dentro del if1 pt2","$RC_GALERY")
            if (requestCode == RC_GALERY){
                mImageSeleccionarVendedor = data?.data

                Log.d("esto es antes del erro", mImageSeleccionarVendedor.toString())
                mBinding.ImgVendedro.setImageURI(mImageSeleccionarVendedor)
            }else{
                Snackbar.make(mBinding.root, "Mensaje2", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}