package com.dimantik.tyteda.ui.launcher

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dimantik.tyteda.R
import com.dimantik.tyteda.databinding.FragmentLauncherBinding

class LauncherFragment : Fragment() {

    private lateinit var binding: FragmentLauncherBinding
    private lateinit var viewModel: LauncherViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentLauncherBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        binding = this
        init()
    }.root

    private fun init() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            findNavController().navigate(R.id.menuFragment)
        }, 3000)
    }


}