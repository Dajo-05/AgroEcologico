package com.example.agroecologico.FragmentosVendedor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.agroecologico.R
import com.example.agroecologico.databinding.FragmentHomeVendedorBinding

class HomeVendedorFragment : Fragment() {

    private lateinit var mbinding: FragmentHomeVendedorBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mbinding= FragmentHomeVendedorBinding.inflate(inflater, container, false)
        return mbinding.root
    }


}