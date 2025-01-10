package com.kaushalpanjee.common

import android.os.Bundle
import android.view.View
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.databinding.ChangePassFragmentBinding

class ChangePasswordFragment : BaseFragment<ChangePassFragmentBinding>(ChangePassFragmentBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init(){

        listener()

    }

    private  fun listener(){


    }
}