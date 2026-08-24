package com.kaushalpanjee.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.kaushalpanjee.common.compose.helpdesk.RequesterTicketScreen
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.toastLong
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HelpDeskFragment : Fragment() {

    private val commonViewModel: CommonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {

                MaterialTheme {

                    RequesterTicketScreen {

                        findNavController().navigateUp()
                    }

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectCreateTicketResponse()

    }

    private fun collectCreateTicketResponse() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                commonViewModel
                    .createTicket
                    .collectLatest { result ->

                        when (result) {

                            is Resource.Loading -> {

                               // showProgressBar()
                            }

                            is Resource.Success -> {

                               // hideProgressBar()

                                result.data?.let { response ->

                                    if (response.responseCode == 200) {

                                        toastLong(
                                            response.responseDesc
                                                ?: "Ticket created successfully"
                                        )

                                       // dialog?.dismiss()

                                    } else {

                                        toastLong(
                                            response.responseDesc
                                                ?: "Unable to create ticket"
                                        )
                                    }
                                }
                            }

                            is Resource.Error -> {

                              //  hideProgressBar()

                                toastLong(
                                    result.error?.message
                                        ?: "Something went wrong"
                                )
                            }
                        }
                    }
            }
        }
    }

}