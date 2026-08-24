package com.kaushalpanjee.common.compose.helpdesk

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RaiseTicketBottomSheet(

    onDismiss: () -> Unit,

    onSubmit: (
        ticketType: String,
        scheme: String,
        title: String,
        description: String,
        attachment: Uri?
    ) -> Unit

) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Surface(

            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(
                    horizontal = 14.dp,
                    vertical = 12.dp
                )
                .fillMaxHeight(.90f),

            shape = RoundedCornerShape(28.dp),

            color = Color.White,

            shadowElevation = 10.dp

        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                HelpDeskHeader(
                    onClose = onDismiss
                )

                HorizontalDivider(
                    color = Color(0xFFEAEAEA)
                )

                CreateTicketContent(

                    onSubmit = {
                            ticketType,
                            scheme,
                            title,
                            description,
                            attachment ->

                        onSubmit(
                            ticketType,
                            scheme,
                            title,
                            description,
                            attachment
                        )
                    }
                )
            }
        }
    }
}