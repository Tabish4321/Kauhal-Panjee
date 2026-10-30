package com.kaushalpanjee.common.compose.registration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegistrationSuccessPopup(
    bitmap: Bitmap,
    candidateName: String,
    candidateGender: String,
    candidateDob: String,
    candidateCareOf: String,
    onContinue: () -> Unit
) {
    val darkGreen = Color(0xFF176B45)
    val lightGreen = Color(0xFFEAF6EF)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White,
                RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            )
            .padding(horizontal = 22.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Small drag indicator
        Box(
            modifier = Modifier
                .width(38.dp)
                .height(4.dp)
                .background(
                    Color.LightGray,
                    RoundedCornerShape(10.dp)
                )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Candidate photo
        Box(
            modifier = Modifier.size(94.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Candidate Photo",
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .border(2.dp, darkGreen, CircleShape),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .size(27.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Verified",
                    tint = darkGreen,
                    modifier = Modifier.size(25.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Success heading
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Registration Completed",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = darkGreen
            )

            Spacer(modifier = Modifier.width(5.dp))

            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = darkGreen,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Your Aadhaar verification was successful.",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Candidate details card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFFF7F9F8),
                    RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 15.dp, vertical = 12.dp)
        ) {
            RegistrationDetailRow("Name", candidateName, darkGreen)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color(0xFFE6EBE8)
            )

            RegistrationDetailRow("Care Of", candidateCareOf, darkGreen)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color(0xFFE6EBE8)
            )

            RegistrationDetailRow("Gender", candidateGender, darkGreen)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color(0xFFE6EBE8)
            )

            RegistrationDetailRow("Date of Birth", candidateDob, darkGreen)
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Continue button
        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = darkGreen
            )
        ) {
            Text(
                text = "Continue",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
    }
}