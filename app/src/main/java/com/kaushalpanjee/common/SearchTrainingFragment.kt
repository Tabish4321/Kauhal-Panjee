package com.kaushalpanjee.common

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.common.model.request.GetSearchTraining
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.databinding.SearchTrainingFragmentBinding
import kotlinx.coroutines.launch

class SearchTrainingFragment : BaseFragment<SearchTrainingFragmentBinding>(SearchTrainingFragmentBinding::inflate) {


    private val commonViewModel: CommonViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init()
        val centerCode = arguments?.getString("centerCode")
        commonViewModel.getSelectedTrainingListAPI(GetSearchTraining(BuildConfig.VERSION_NAME,centerCode))
        collectSelectedTrainingSearchResponse()
    }

    private  fun  init(){

        listener()

    }

    private fun listener(){






    }


    private fun collectSelectedTrainingSearchResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getSelectedTrainingListAPI) {
                when (it) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        it.error?.let { baseErrorResponse ->
                            showSnackBar(baseErrorResponse.message)
                        }
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { getSelectedTrainingListAPI ->
                            if (getSelectedTrainingListAPI.responseCode == 200) {
                                val trainingList = getSelectedTrainingListAPI.centerList

                                for (x in trainingList){

                                    binding.address.setText(x.address)
                                    binding.centerName.setText(x.centerName)
                                    binding.contactNo.setText(x.contactNo)

                                    val bytes: ByteArray =
                                        Base64.decode(x.centerImage, Base64.DEFAULT)
                                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                                    binding.centerImage.setImageBitmap(bitmap)

                                }

                            } else if (getSelectedTrainingListAPI.responseCode == 301) {
                                showSnackBar("Please Update from PlayStore")
                            } else {
                                showSnackBar("Something went wrong")
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }

}