package com.example.myapplication.CBT


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.CBT.api.Option

@Composable
fun OptionItem(
    option: Option,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        border = if (selected)
            BorderStroke(2.dp, Color(0xFF36D1A6))
        else null,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    )
    {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF36D1A6),
                    unselectedColor = Color(0xFF2A4D44)
                )
            )
//            RadioButton(
//                selected = selected,
//                onClick = onClick
//            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {

                Text(
                    text = "${option.option_key}. ${option.option_value}",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

//@Composable
//fun OptionItem(
//    option: Option,
//    selected: Boolean,
//    onClick: () -> Unit
//) {
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 6.dp, horizontal = 16.dp)
//            .clip(RoundedCornerShape(14.dp))
//            .background(
//                if (selected)
//                    Brush.horizontalGradient(
//                        listOf(
//                            Color(0xFF36D1A6),
//                            Color(0xFF4F9488),
//                            Color(0xFF2A4D44)
//                        )
//                    )
//                else
//                    Brush.horizontalGradient(
//                        listOf(Color.White, Color.White)
//                    )
//            )
//            .border(
//                width = 1.dp,
//                color = if (selected)
//                    Color.Transparent
//                else
//                    Color(0xFFE0E0E0),
//                shape = RoundedCornerShape(14.dp)
//            )
//            .clickable { onClick() }
//            .padding(16.dp)
//    ) {
//
//        Text(
//            text = "${option.option_key}. ${option.option_value}",
//            color = if (selected) Color.White else Color.Black,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}



//@Composable
//fun OptionItem(
//    option: Option,
//    selected: Boolean,
//    onClick: () -> Unit
//) {
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 6.dp, horizontal = 16.dp)
//            .clip(RoundedCornerShape(14.dp))
//            .background(
//                if (selected)
//                    Brush.horizontalGradient(
//                        listOf(
//                            Color(0xFF36D1A6),
//                            Color(0xFF4F9488),
//                            Color(0xFF2A4D44)
//                        )
//                    )
//                else
//                    Brush.horizontalGradient(
//                        listOf(Color.White, Color.White)
//                    )
//            )
//            .border(
//                width = 1.dp,
//                color = if (selected)
//                    Color.Transparent
//                else
//                    Color(0xFFE0E0E0),
//                shape = RoundedCornerShape(14.dp)
//            )
//            .clickable { onClick() }
//            .padding(16.dp)
//    ) {
//
//        Text(
//            text = "${option.option_key}. ${option.option_value}",
//            color = if (selected) Color.White else Color.Black,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}



//@Composable
//fun OptionItem(
//    options: List<Option>,
//    selectedKey: String?,
//    onOptionSelected: (Option) -> Unit
//) {
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        horizontalArrangement = Arrangement.SpaceEvenly
//    ) {
//
//        options.forEach { option ->
//
//            val isSelected = selectedKey == option.option_key
//
//            Box(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(4.dp)
//                    .clip(RoundedCornerShape(12.dp))
//                    .background(
//                        if (isSelected)
//                            Brush.horizontalGradient(
//                                listOf(
//                                    Color(0xFF36D1A6),
//                                    Color(0xFF4F9488),
//                                    Color(0xFF2A4D44)
//                                )
//                            )
//                        else
//                            Brush.horizontalGradient(
//                                listOf(Color.White, Color.White)
//                            )
//                    )
//                    .border(
//                        width = 1.dp,
//                        color = if (isSelected)
//                            Color.Transparent
//                        else
//                            Color.Gray,
//                        shape = RoundedCornerShape(12.dp)
//                    )
//                    .clickable { onOptionSelected(option) }
//                    .padding(vertical = 12.dp),
//                contentAlignment = Alignment.Center
//            ) {
//
//                Text(
//                    text = option.option_key,
//                    color = if (isSelected) Color.White else Color.Black,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
//    }
//}




//@Composable
//fun OptionItem(
//    option: Option,
//    selected: Boolean,
//    onClick: () -> Unit
//) {
//
//    Card(
//        border = if (selected)
//            BorderStroke(2.dp, Color(0xFF7B1FA2))
//        else null,
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        ),
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onClick() }
//    ) {
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(16.dp)
//        ) {
//
//            RadioButton(
//                selected = selected,
//                onClick = onClick
//            )
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            Column {
//
//                Text(
//                    text = "${option.option_key}. ${option.option_value}",
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
//    }
//}