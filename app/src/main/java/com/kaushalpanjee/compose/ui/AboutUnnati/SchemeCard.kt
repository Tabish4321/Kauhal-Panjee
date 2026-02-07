package com.kaushalpanjee.compose.ui.AboutUnnati

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushalpanjee.R
import com.kaushalpanjee.compose.domain.model.Scheme
import com.kaushalpanjee.compose.ui.commonComponent.KPSpacer
import com.kaushalpanjee.compose.ui.commonComponent.KPText

/**
 * Created by Rishi Porwal
 */

@Composable
fun SchemeCard(
    scheme: Scheme,
    expanded: Boolean,
    onClick: () -> Unit,
    count :Int
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() }
                    .background(colorResource(id = R.color.color_dark_light_green))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                KPText("${count+1}", color= colorResource(id = R.color.white))

                KPSpacer(7.dp)
//                Box(
//                    modifier = Modifier
//                        .size(6.dp)
//                        .background(
//                            colorResource(id = R.color.white),
//                            shape = CircleShape
//                        )
//                        .align(Alignment.CenterVertically)
//                )

                KPText(scheme.name, color= colorResource(id = R.color.white), modifier = Modifier.weight(1f))

                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier.rotate(
                        if (expanded) 90f else 0f
                    )
                )
            }

            AnimatedVisibility(expanded) {
                Column(
                    modifier = Modifier
                        .background(colorResource(id = R.color.color_light_grey))
                        .padding(12.dp)
                ) {

                    VideoPlayer(url = scheme.videoUrl)

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp),
                        thickness = 1.dp,
                        color = colorResource(id = R.color.color_sub_text)
                    )

                    KPSpacer(10.dp,true)

                    scheme.details.forEach {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            KPText(".", fontSize = 12.sp)
                            KPSpacer(7.dp)
                            KPText(it)
                        }
                    }
                }
                KPSpacer(7.dp,true)

            }
        }
    }
}
