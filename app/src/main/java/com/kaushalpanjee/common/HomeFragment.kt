package com.kaushalpanjee.common

import android.R
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.animation.addListener
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private var isSeccVisible = false

    private lateinit var districtAdapter: ArrayAdapter<String>
    private val district = ArrayList<String>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        listener()
    }

    private fun listener() {

        binding.llTopPersonal.setOnClickListener {

            if (isSeccVisible){
                isSeccVisible = false
               // binding.clSecc.gone()
                binding.viewSecc.gone()
            }else {
                isSeccVisible = true
             //   binding.clSecc.visible()
                binding.viewSecc.visible()
            }
        }


        district.add("Barpeta")
        district.add("Jorhat")
        district.add("Kamrup")

        // Create an ArrayAdapter
        districtAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, district)
      //  districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter to the Spinner
        binding.spinnerDistrict.setAdapter(districtAdapter)

        // Handle item selection
        binding.spinnerDistrict.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedItem = district[position]
                Toast.makeText(requireContext(), "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                // Handle case where no item is selected
            }
        }
    }

    }
