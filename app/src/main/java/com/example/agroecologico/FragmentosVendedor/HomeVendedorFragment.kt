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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
import com.example.agroecologico.R
import com.squareup.picasso.Picasso

class HomeVendedorFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeVendedorBinding



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

        mStoreReference= FirebaseStorage.getInstance().reference
        leerDato()
        mBinding.BtnGuardar.setOnClickListener { modificar() }
    }

    private fun leerDato() {
        val userId = auth.currentUser?.uid.toString()
        mBinding.progressBar.visibility= View.VISIBLE
        val dbpuesto= FirebaseDatabase.getInstance().reference.child("PuestoVenta")
        val recuperar=object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    mBinding.MiPV.text=it.child("nombrePuesto").getValue().toString()
                    usuario= PuestoVentaData(idpuesto = it.child("idpuesto").getValue().toString()
                        , nombrePuesto =  it.child("nombrePuesto").getValue().toString(),
                        telefono =  it.child("telefono").getValue().toString(),
                        correo =  it.child("correo").getValue().toString(),
                        whatsapp= it.child("whatsapp").getValue().toString(),
                        telegran =  it.child("telegram").getValue().toString(),
                        foto =  it.child("foto").getValue().toString(),
                        vendedor1 = it.child("vendedor1").getValue().toString(),
                        imgVendedor1 = it.child("imgVendedor1").getValue().toString())
                }
                mBinding.MiPV.text.toString()
                //Glide.with(context!!).load(usuario.foto).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(mBinding.ImgTerreno)
                //Glide.with(context!!).load(usuario.imgVendedor1).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(mBinding.Vendedor2)
                Picasso.get()
                    .load(usuario.foto)
                    .fit()
                    .into(mBinding.ImgTerreno)
                mBinding.progressBar.visibility= View.GONE
                //Log.d("Datos leidos","${usuario}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error",error.toString())
            }

        }
        dbpuesto.addValueEventListener(recuperar)
    }


    private fun modificar() {
        val editar = VendedorEditPuestoFragment()
        val transacion = fragmentManager?.beginTransaction()
        transacion?.replace(R.id.flMain, editar)
            ?.addToBackStack(null)
            ?.commit()
    }




}



