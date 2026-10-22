package com.kaushalpanjee.common.compose.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushalpanjee.R

@Composable
fun LoginHeader(
    appName: String = stringResource(R.string.app_name),
    department: String = stringResource(R.string.app_department)
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_ddgky),
                contentDescription = stringResource(R.string.login_ddugky_logo),
                modifier = Modifier.size(width = 112.dp, height = 61.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.width(10.dp))

            Box(
                Modifier
                    .width(1.dp)
                    .height(38.dp)
                    .background(LoginColors.Border)
            )

            Spacer(Modifier.width(10.dp))

            Image(
                painter = painterResource(R.drawable.ic_rseti),
                contentDescription = stringResource(R.string.login_rseti_logo),
                modifier = Modifier.size(width = 112.dp, height = 61.dp),
                contentScale = ContentScale.Fit
            )
        }

        Text(
            text = appName,
            modifier = Modifier.padding(top = 4.dp),
            color = LoginColors.DarkGreen,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        Row(
            modifier = Modifier.padding(top = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .width(43.dp)
                    .height(1.dp)
                    .background(LoginColors.Border)
            )

            Box(
                Modifier
                    .padding(horizontal = 7.dp)
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(LoginColors.DarkGreen)
            )

            Box(
                Modifier
                    .width(43.dp)
                    .height(1.dp)
                    .background(LoginColors.Border)
            )
        }

        Text(
            text = department,
            modifier = Modifier.padding(top = 3.dp),
            color = LoginColors.TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}