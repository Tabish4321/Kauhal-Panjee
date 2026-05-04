package com.kaushalpanjee.common.compose

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.kaushalpanjee.core.util.AppUtil.base64ToBitmap

// ================= PREMIUM CARD =================
@Composable
fun PremiumCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

// ================= NEW COMMON INPUT =================
@Composable
fun CommonInput(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    keyboard: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    Column {

        Text(
            text = label,
            fontWeight = FontWeight.Bold, // ✅ bold
            modifier = Modifier.padding(top = 6.dp, bottom = 4.dp) // ✅ margin
        )

        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter $label") },
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboard),
            visualTransformation = if (isPassword)
                PasswordVisualTransformation()
            else VisualTransformation.None,
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))
    }
}




// ================= OLD INPUT (FOR BACKWARD) =================
@Composable
fun InputField(label: String) {
    var text by remember { mutableStateOf("") }

    CommonInput(
        label = label,
        value = text,
        onChange = { text = it }
    )
}

// ================= FIXED (THIS WAS MISSING) =================
@Composable
fun InputFieldWithKeyboard(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType
) {
    CommonInput(
        label = label,
        value = value,
        onChange = onValueChange,
        keyboard = keyboardType
    )
}

// ================= DROPDOWN (NEW) =================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonDropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Column {

        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 6.dp, bottom = 4.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Select $label") },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                }
            )

            // ✅ WHITE BACKGROUND
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                options.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            onSelect(it)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))
    }
}

// ================= OLD DROPDOWN SUPPORT =================
@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    CommonDropdown(label, options, selected, onSelected)
}

// ================= STEP HEADER =================

@Composable
fun StepHeader(
    step: Int,
    totalSteps: Int
) {

    val listState = rememberLazyListState()

    //  Auto scroll to current step
    LaunchedEffect(step) {
        listState.animateScrollToItem(step)
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        items(totalSteps + 1) { index ->

            val isDone = index < step
            val isActive = index == step

            val color by animateColorAsState(
                targetValue = when {
                    isDone -> Color(0xFF4CAF50)
                    isActive -> MaterialTheme.colorScheme.primary
                    else -> Color.LightGray
                },
                label = ""
            )

            val scale by animateFloatAsState(
                targetValue = if (isActive) 1.2f else 1f,
                label = ""
            )

            Row(verticalAlignment = Alignment.CenterVertically) {

                Surface(
                    shape = CircleShape,
                    color = color,
                    modifier = Modifier
                        .size(34.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                ) {
                    Box(contentAlignment = Alignment.Center) {

                        if (isDone) {
                            Icon(Icons.Default.Check, null, tint = Color.White)
                        } else {
                            Text("${index + 1}", color = Color.White)
                        }
                    }
                }

                //  connector line
                if (index < totalSteps) {

                    val lineColor by animateColorAsState(
                        targetValue = if (index < step)
                            Color(0xFF4CAF50)
                        else Color.LightGray,
                        label = ""
                    )

                    Box(
                        Modifier
                            .width(40.dp)
                            .height(2.dp)
                            .background(lineColor)
                    )
                }
            }
        }
    }
}
// ================= UPLOAD =================
@Composable
fun UploadItem(title: String) {

    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bytes = context.contentResolver.openInputStream(it)?.readBytes()
            Base64.encodeToString(bytes, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
        }
    }

    val permission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) picker.launch("image/*")
    }

    Column {

        Text("Upload $title")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(vertical = 8.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(1.dp, Color.Red, RoundedCornerShape(12.dp))
                .clickable {

                    val perm = if (Build.VERSION.SDK_INT >= 33)
                        Manifest.permission.READ_MEDIA_IMAGES
                    else
                        Manifest.permission.READ_EXTERNAL_STORAGE

                    if (ContextCompat.checkSelfPermission(context, perm)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        picker.launch("image/*")
                    } else {
                        permission.launch(perm)
                    }
                },
            contentAlignment = Alignment.Center
        ) {

            if (bitmap != null) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CloudUpload, null, tint = Color.Red)
                    Spacer(Modifier.height(8.dp))
                    Text("Upload Photograph")
                }
            }
        }
    }
}
@Composable
fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String?
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {

        // 🔹 Consistent Icon Badge (Matches Header)
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 1.dp,
            modifier = Modifier.size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            // 🔹 Label
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(6.dp))

            // 🔹 Value
            Text(
                text = value!!.ifBlank { "—" },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}



@Composable
fun BottomButtons(
    step: Int,
    onNext: () -> Unit,
    onBack: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        //  Previous Button (Left)
        if (step > 0) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .height(52.dp)
                    .width(130.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Previous")
            }
        } else {
            Spacer(modifier = Modifier.width(130.dp))
        }

        //  Next Button (Right)
        Button(
            onClick = onNext,
            modifier = Modifier
                .height(52.dp)
                .width(150.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(if (step == 9) "Submit" else "Next")
        }
    }
}


@Composable
fun PanInputField(
    pan: String,
    onChange: (String) -> Unit,
    isValid: Boolean
) {

    Column {

        Text(
            "Enter PAN Number",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 6.dp, bottom = 4.dp)
        )

        OutlinedTextField(
            value = pan,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("ABCDE1234F") },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,

            trailingIcon = {
                if (pan.isNotEmpty()) {
                    Icon(
                        imageVector = if (isValid)
                            Icons.Default.CheckCircle
                        else
                            Icons.Default.Error,
                        contentDescription = null,
                        tint = if (isValid)
                            Color(0xFF4CAF50)
                        else
                            Color.Red
                    )
                }
            }
        )

        if (!isValid && pan.length >= 10) {
            Text(
                text = "Invalid PAN format",
                color = Color.Red,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(Modifier.height(12.dp))
    }
}


@Composable
fun ImagePreviewDialog(
    base64Image: String,
    onDismiss: () -> Unit
) {

    val bitmap = remember(base64Image) {
        base64ToBitmap(base64Image)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },

        text = {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            } else {
                Text("Image not available")
            }
        }
    )
}