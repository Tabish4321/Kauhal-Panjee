package com.kaushalpanjee.common

import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private var isPersonalVisible = true
    private var isAddressVisible = true
    private var isEducationalInfoVisible = true
    private var isEmploymentInfoVisible = true

    private lateinit var districtAdapter: ArrayAdapter<String>
    private val district = ArrayList<String>()

    // Calendar instance to get current date
    private val calendar: Calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }



    private fun init() {
        listener()
        districtSPinner()
    }

    @SuppressLint("SetTextI18n")
    private fun listener() {

        binding.cpvHome.setProgress(50f)
        binding.cpvHome.setProgressWithAnimation(50f)

        binding.llTopPersonal.setOnClickListener {

            if (isPersonalVisible){
                isPersonalVisible = false
                binding.personalExpand.visible()
                binding.viewSecc.visible()
            }else {
                isPersonalVisible = true
                binding.personalExpand.gone()
                binding.viewSecc.gone()
            }
        }

        binding.llTopAddress.setOnClickListener {

            if (isAddressVisible){
                isAddressVisible = false
                binding.expandAddress.visible()
                binding.viewAddress.visible()
            }else {
                isAddressVisible = true
                binding.expandAddress.gone()
                binding.viewAddress.gone()
            }
        }

        binding.llTopEducational.setOnClickListener {

            if (isEducationalInfoVisible){
                isEducationalInfoVisible = false
                binding.expandEducational.visible()
                binding.viewEducational.visible()
            }else {
                isEducationalInfoVisible = true
                binding.expandEducational.gone()
                binding.viewEducational.gone()
            }
        }

        binding.llTopEmployment.setOnClickListener {

            if (isEmploymentInfoVisible){
                isEmploymentInfoVisible = false
                binding.expandEmployment.visible()
                binding.viewEmployment.visible()
            }else {
                isEmploymentInfoVisible = true
                binding.expandEmployment.gone()
                binding.viewEmployment.gone()
            }
        }


        binding.tvClickYearOfPassing.setOnClickListener {

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Save and display the selected date
                    val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    binding.tvClickYearOfPassing.text = formattedDate.toString()
                },
                year, month, day
            )
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            datePickerDialog.show()
            datePickerDialog.show()
        }

        binding.tvClickYearOfPassingTech.setOnClickListener {

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Save and display the selected date
                    val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    binding.tvClickYearOfPassingTech.text = formattedDate.toString()
                },
                year, month, day
            )
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            datePickerDialog.show()
            datePickerDialog.show()
        }

        binding.tvClickPreviouslycompletedduring.setOnClickListener {

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Save and display the selected date
                    val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    binding.tvClickPreviouslycompletedduring.text = formattedDate.toString()
                },
                year, month, day
            )
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            datePickerDialog.show()
            datePickerDialog.show()
        }




    }
    fun districtSPinner(){
        district.add("Siwan")
        district.add("muzzafarpur")
        district.add("Chapra")

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
