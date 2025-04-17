package com.kaushalpanjee.common

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.kaushalpanjee.common.model.MultiLanguage
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.UserPreferences
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.FragmentLanguageChangeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
@AndroidEntryPoint

class LanguageChangeFragment :BaseFragment<FragmentLanguageChangeBinding> (FragmentLanguageChangeBinding :: inflate)  {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        if (AppUtil.getSavedLanguagePreference(requireContext()).contains("en")){

            binding.checkEnglishIcon.visible()
            binding.checkIconHindi.gone()



        }
        else if (AppUtil.getSavedLanguagePreference(requireContext()).contains("hi")){
            binding.checkIconHindi.visible()
            binding.checkEnglishIcon.gone()

        }
        else
            binding.checkEnglishIcon.visible()

        binding.languageEng.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Do you want to change language?",
                onYesClicked = {
                    // Action for Yes button

                    AppUtil.changeAppLanguage(requireContext(),"en")
                    lifecycleScope.launch {
                      AppUtil.saveLanguagePreference(requireContext(),"en")
                        binding.checkEnglishIcon.visible()
                        binding.checkIconHindi.gone()




                    }



                },
                onNoClicked = {

                }
            )
        }

        binding.languageHindi.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Do you want to change language?",
                onYesClicked = {
                    // Action for Yes button
                    lifecycleScope.launch{

                    AppUtil.changeAppLanguage(requireContext(),"hi")
                    binding.checkIconHindi.visible()
                        binding.checkEnglishIcon.gone()
                        AppUtil.saveLanguagePreference(requireContext(),"hi")



                    }



                },
                onNoClicked = {

                }
            )

        }


        binding.languageTamil.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Coming soon",
                onYesClicked = {
                    /*    // Action for Yes button
                        lifecycleScope.launch{

                            AppUtil.changeAppLanguage(requireContext(),"hi")
                            binding.checkIconHindi.visible()
                            binding.checkEnglishIcon.gone()
                            AppUtil.saveLanguagePreference(requireContext(),"hi")

                            findNavController().navigateUp()


                        }*/



                },
                onNoClicked = {

                }
            )

        }
        binding.languageAssamese.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Coming soon",
                onYesClicked = {
                    /*    // Action for Yes button
                        lifecycleScope.launch{

                            AppUtil.changeAppLanguage(requireContext(),"hi")
                            binding.checkIconHindi.visible()
                            binding.checkEnglishIcon.gone()
                            AppUtil.saveLanguagePreference(requireContext(),"hi")

                            findNavController().navigateUp()


                        }*/



                },
                onNoClicked = {

                }
            )

        }
    }

    fun showYesNoDialog(context: Context, title: String, message: String, onYesClicked: () -> Unit, onNoClicked: () -> Unit) {
        // Create the AlertDialog.Builder
        val builder = AlertDialog.Builder(context)

        // Set the title and message
        builder.setTitle(title)
        builder.setMessage(message)

        // Set the positive button (Yes)
        builder.setPositiveButton("Yes") { dialog, _ ->
            onYesClicked() // Execute the Yes action
            dialog.dismiss()
        }

        // Set the negative button (No)
        builder.setNegativeButton("No") { dialog, _ ->
            onNoClicked() // Execute the No action
            dialog.dismiss()
        }

        // Optionally set the dialog to be cancelable (can be canceled by clicking outside)
        builder.setCancelable(true)

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }




}





