package com.kaushalpanjee.common.compose.helpdesk

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width

@Composable
fun CommonButton(

    text: String,

    modifier: Modifier = Modifier,

    onClick: () -> Unit

) {

    Button(

        onClick = onClick,

        modifier = modifier
            .fillMaxWidth()
            .height(55.dp),

        shape = RoundedCornerShape(14.dp),

        colors = ButtonDefaults.buttonColors(

            containerColor = Color(0xFF173430)

        )

    ) {

        Icon(

            imageVector = Icons.Default.Send,

            contentDescription = null

        )

        Spacer(

            modifier = Modifier.width(8.dp)

        )

        Text(

            text = text

        )

    }

}