package com.kaushalpanjee.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.request.BannerReq
import com.kaushalpanjee.common.model.request.SectionAndPerReq
import com.kaushalpanjee.common.model.request.TrainingSearch
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.createHalfCircleProgressBitmap
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.FragmentMainHomeBinding
import com.kaushalpanjee.databinding.NavigationHeaderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainHomePage : BaseFragment<FragmentMainHomeBinding>(FragmentMainHomeBinding::inflate) {


    private lateinit var adapter: AdvertiseCardAdapter
    private val handler = Handler()
    private var scrollPosition = 0
    private val commonViewModel: CommonViewModel by activityViewModels()

    private var personalStatus = ""
    private var imagePath = ""
    private var educationalStatus = ""
    private var trainingStatus = ""
    private var seccStatus = ""
    private var addressStatus = ""
    private var employmentStatus = ""
    private var bankingStatus = ""
    private var selectedTrainingName = ""
    private var totalPercentange =0.0f
    private var bannerImageList = ArrayList<String>()

    private val searchQuery = MutableLiveData<String>()
    private lateinit var trainingSearchAdapter: TrainingSearchAdapter




    private val bannerImageBitmapList = mutableListOf<Bitmap>()




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBackPress()
        init()
        commonViewModel.getBannerAPI(AppUtil.getSavedTokenPreference(requireContext()),BannerReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext())))
        collectBannerResponse()


    }
     private fun init(){




         val drawerLayout = binding.drawerLayout



         binding.navigationView.setNavigationItemSelectedListener { menuItem ->
             when (menuItem.itemId) {

                 R.id.logout -> {
                     // Handle profile click

                     AppUtil.saveLoginStatus(requireContext(), false)  // false means user is logged out

                     findNavController().navigate(
                         R.id.loginFragment,
                         null,
                         NavOptions.Builder()
                             .setPopUpTo(R.id.mainHomePage, true)
                             .build()
                     )
                 }

                 R.id.changePass -> {

                     //For change password

                     findNavController().navigate(MainHomePageDirections.actionMainHomePageToChangePasswordFragment())

                 }

             }
             drawerLayout.closeDrawers()
             true
         }

         binding.trainingRecyclerView.gone()


         listeners()
         autoScroll()
         commonViewModel.getSecctionAndPerAPI(SectionAndPerReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext())),AppUtil.getSavedTokenPreference(requireContext()))


         collectSetionAndPerResponse()
         collectTrainingSearchResponse()




     }

 private fun  listeners(){




    //Training Adapter Setting

     binding.trainingRecyclerView.layoutManager = LinearLayoutManager(requireContext())
     trainingSearchAdapter = TrainingSearchAdapter { selectedItem ->
         selectedTrainingName = selectedItem.centerName
         val selectedTrainingCenterCode = selectedItem.centerCode
         binding.etSearch.setText("")
         findNavController().navigate(MainHomePageDirections.actionMainHomePageToSearchTrainingFragment(selectedTrainingCenterCode))

     }

     binding.changeLanguage.setOnClickListener {
         findNavController().navigate(MainHomePageDirections.actionMainHomePageToLanguageChangeFragment())

     }

     binding.trainingRecyclerView.adapter = trainingSearchAdapter

     // Training Search Text
     searchQuery.observe(viewLifecycleOwner) { query ->
         if (query.length >= 4) {
             handleTrainingSearchQuery(query) // Trigger API call

         }
         else  binding.trainingRecyclerView.gone()

     }

     // Add TextWatcher to EditText
     binding.etSearch.addTextChangedListener(object : TextWatcher {
         override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

         override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
             searchQuery.value = s.toString()
         }

         override fun afterTextChanged(s: Editable?) {}
     })






     binding.trainingImageLogo.setOnClickListener {

         findNavController().navigate(MainHomePageDirections.actionMainHomePageToTrainingFragment())
     }




     binding.personalImageLogo.setOnClickListener {
             findNavController().navigate(MainHomePageDirections.actionMainHomePageToViewDetailsFragment())

     }

         binding.circleImageViewMH.setOnClickListener {
             binding.drawerLayout.openDrawer(GravityCompat.START)

         }

     binding.tvCompleteNow.setOnClickListener {

         lifecycleScope.launch {

             showProgressBar()
             findNavController().navigate(MainHomePageDirections.actionMainHomePageToHomeFragment())
             delay(2000)
             hideProgressBar()

         }
     }

     val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
     binding.recyclerView.layoutManager = layoutManager

     // Set up the adapter with dummy data
     adapter = AdvertiseCardAdapter(bannerImageBitmapList)
     binding.recyclerView.adapter = adapter
     // Start auto-scrolling
 }


    private fun autoScroll(){
        val scrollRunnable = Runnable {
            // Scroll to the next position
            if (scrollPosition < adapter.itemCount) {
                binding.recyclerView.smoothScrollToPosition(scrollPosition)
                scrollPosition++
            }

            if (scrollPosition >= adapter.itemCount) {
                // Reset scroll position if we reach the end of the list
                scrollPosition = 0
            }
        }

        // Post the scrolling task initially and periodically every 2 seconds
        handler.postDelayed(object : Runnable {
            override fun run() {
                scrollRunnable.run()
                handler.postDelayed(this, 3000) // Scroll every 2 seconds
            }
        }, 0) // Delay the first scroll action by 2 seconds
    }

    // Cleanup handler to stop auto-scrolling when activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Remove all pending posts
    }


    private fun collectSetionAndPerResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getSecctionAndPerAPI) {
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
                        it.data?.let { getSecctionAndPerAPI ->
                            if (getSecctionAndPerAPI.responseCode == 200) {
                                val percentageList = getSecctionAndPerAPI.wrappedList

                                for (x in percentageList) {

                                    personalStatus= x.personalStatus.toString()
                                    educationalStatus= x.educationalStatus.toString()
                                    trainingStatus= x.trainingStatus.toString()
                                    seccStatus= x.seccStatus .toString()
                                    addressStatus= x.addressStatus.toString()
                                    employmentStatus= x.employmentStatus.toString()
                                    bankingStatus= x.bankingStatus.toString()
                                    totalPercentange= x.totalPercentage
                                    imagePath= x.imagePath

                                }

                                binding.ivMeter.setImageBitmap(createHalfCircleProgressBitmap(300,300,totalPercentange,
                                    ContextCompat.getColor(requireContext(),R.color.color_FFFFFFB3),
                                    ContextCompat.getColor(requireContext(),R.color.white),35f,20f,
                                    ContextCompat.getColor(requireContext(),R.color.black),
                                    ContextCompat.getColor(requireContext(),R.color.color_dark_green)))


                                //Setting Status
                                if (personalStatus.contains("1")){
                                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_dark_verified)

                                    binding.tvPersonalDetails.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                                    binding.tvPersonalDetails.setCompoundDrawablePadding(16)
                                }

                                else{
                                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_cancel)

                                    binding.tvPersonalDetails.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                                    binding.tvPersonalDetails.setCompoundDrawablePadding(16)
                                }



                                if (addressStatus.contains("1")){
                                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_dark_verified)

                                    binding.tvAddressDetails.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                                    binding.tvAddressDetails.setCompoundDrawablePadding(16)
                                }

                                else{
                                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_cancel)

                                    binding.tvAddressDetails.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                                    binding.tvAddressDetails.setCompoundDrawablePadding(16)
                                }


                                if (bankingStatus.contains("1")){
                                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_dark_verified)

                                    binding.tvBankingDetails.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                                    binding.tvBankingDetails.setCompoundDrawablePadding(16)
                                }

                                else{
                                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_cancel)

                                    binding.tvBankingDetails.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                                    binding.tvBankingDetails.setCompoundDrawablePadding(16)
                                }


                                if (imagePath!=null){

                                    val bytes: ByteArray =
                                        Base64.decode(imagePath, Base64.DEFAULT)
                                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                    binding.circleImageViewMH.setImageBitmap(bitmap)


                                    // First, get the header view using getHeaderView()
                                    val headerView = binding.navigationView.getHeaderView(0)

                                    // Now, bind the header layout using the generated ViewBinding for the header
                                    val headerBinding = NavigationHeaderBinding.bind(headerView)

                                    // Access the ImageView from the header layout
                                    val headerImageView: ImageView = headerBinding.circleImageView

                                    headerImageView.setImageBitmap(bitmap)


                                }



                            } else if (getSecctionAndPerAPI.responseCode == 301) {
                                showSnackBar("Please Update from PlayStore")
                            } else if (getSecctionAndPerAPI.responseCode == 401) {
                                AppUtil.showSessionExpiredDialog(findNavController(),requireContext())

                            } else {
                                showSnackBar("Something went wrong")
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }

        private fun collectTrainingSearchResponse() {
            lifecycleScope.launch {
                collectLatestLifecycleFlow(commonViewModel.getTrainingSearchAPI) {
                    when (it) {
                        is Resource.Loading -> showProgressBar()
                        is Resource.Error -> {
                            hideProgressBar()
                            it.error?.let { baseErrorResponse ->
                                showSnackBar("Not Found")
                            }
                        }

                        is Resource.Success -> {
                            hideProgressBar()
                            it.data?.let { getTrainingSearchAPI ->
                                if (getTrainingSearchAPI.responseCode == 200) {
                                    val trainingList = getTrainingSearchAPI.centerList
                                    trainingSearchAdapter.submitList(trainingList)
                                    binding.trainingRecyclerView.visible()

                                } else if (getTrainingSearchAPI.responseCode == 301) {
                                    showSnackBar("Please Update from PlayStore")
                                }   else if (getTrainingSearchAPI.responseCode==401){
                                    AppUtil.showSessionExpiredDialog(findNavController(),requireContext())
                                }else {
                                    showSnackBar("Something went wrong")
                                }
                            } ?: showSnackBar("Internal Server Error")
                        }
                    }
                }
            }
        }

    private fun collectBannerResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getBannerAPI) {
                when (it) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        it.error?.let { baseErrorResponse ->
                            showSnackBar("Not Found")
                        }
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { getBannerAPI ->
                            if (getBannerAPI.responseCode == 200) {
                                val bannerResList = getBannerAPI.bannerList

                                bannerImageBitmapList.clear() // clear old data if any

                                for (banner in bannerResList) {
                                    val bytes: ByteArray =
                                        Base64.decode(banner.bannerImage, Base64.DEFAULT)
                                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                    if (bitmap != null) {
                                        bannerImageBitmapList.add(bitmap)
                                    }
                                }

                                // Now you have list of Bitmap images
                                // You can update your adapter or UI here

                                adapter.notifyDataSetChanged()


                            } else if (getBannerAPI.responseCode == 301) {
                                showSnackBar("Please Update from PlayStore")
                            } else if (getBannerAPI.responseCode == 401) {
                                AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                            } else {
                                showSnackBar("Something went wrong")
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }





    private fun handleTrainingSearchQuery(query: String) {

        commonViewModel.getTrainingSearchAPI(TrainingSearch(BuildConfig.VERSION_NAME,query,userPreferences.getUseID(),
            AppUtil.getAndroidId(requireContext())),AppUtil.getSavedTokenPreference(requireContext()))


    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                private var backPressedTime: Long = 0
                private val exitInterval = 2000 // 2 seconds

                override fun handleOnBackPressed() {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - backPressedTime < exitInterval) {
                        isEnabled =
                            false // Disable callback to let the system handle the back press
                        requireActivity().onBackPressed() // Exit the app
                    } else {
                        backPressedTime = currentTime
                        showSnackBar("Press back again to exit")
                    }
                }
            })
    }

}
