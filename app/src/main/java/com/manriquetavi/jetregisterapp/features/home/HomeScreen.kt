package com.manriquetavi.jetregisterapp.features.home

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.manriquetavi.jetregisterapp.Constant.options
import android.Manifest
import com.manriquetavi.jetregisterapp.domain.model.Form
import com.manriquetavi.jetregisterapp.features.components.FirebaseMessagingNotificationPermissionDialog

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    navigateToTakePicture: () -> Unit,
    onSaveInformation: (Form) -> Unit
) {
    val showNotificationDialog = remember { mutableStateOf(false) }

    // Android 13 Api 33 - runtime notification permission has been added
    val notificationPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )
    if (showNotificationDialog.value) FirebaseMessagingNotificationPermissionDialog(
        showNotificationDialog = showNotificationDialog,
        notificationPermissionState = notificationPermissionState
    )

    LaunchedEffect(key1=Unit){
        if (notificationPermissionState.status.isGranted ||
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
        ) {
            Firebase.messaging.subscribeToTopic("Tutorial")
        } else showNotificationDialog.value = true
    }

    when (homeUiState) {
        is HomeUiState.Success -> {
            HomeContent(
                navigateToTakePicture = navigateToTakePicture,
                onSaveInformation = onSaveInformation
            )
        }

        is HomeUiState.Error -> {

        }

        is HomeUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

}

@Composable
fun HomeContent(
    navigateToTakePicture: () -> Unit,
    onSaveInformation: (Form) -> Unit
) {
    var padding by remember { mutableStateOf(PaddingValues()) }
    var serviceText by remember { mutableStateOf("") }
    var addressText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var completed by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {},
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(end = padding.calculateEndPadding(LayoutDirection.Ltr)),
                onClick = {
                    completed = validateInformation(
                        serviceText = serviceText,
                        addressText = addressText,
                        selectedOption = selectedOption,
                        descriptionText = descriptionText
                    )
                    if (completed) { onSaveInformation(
                        Form(
                            service = serviceText,
                            address = addressText,
                            problem = selectedOption,
                            description = descriptionText
                        )
                    ) }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "New Diary Icon"
                )
            }
        }
    ) { paddingValues ->
        padding = paddingValues
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Text(
                text = "Hospital Sabogal",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = serviceText,
                onValueChange = { serviceText = it },
                placeholder = {
                    Text(
                        modifier = Modifier.alpha(alpha = 0.5f),
                        text = "Servicio"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                singleLine = true
            )
            if (!completed && serviceText.isEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Completar servicio", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = addressText,
                onValueChange = { addressText = it },
                placeholder = {
                    Text(
                        modifier = Modifier.alpha(alpha = 0.5f),
                        text = "Ubicación"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                singleLine = true
            )
            if (!completed && addressText.isEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Completar ubicación", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(Modifier.fillMaxWidth()) {
                TextField(
                    value = selectedOption,
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        }
                        .clickable {
                            expanded = !expanded
                        },
                    trailingIcon = {
                        Icon(
                            icon,
                            "contentDescription",
                            Modifier.clickable { expanded = !expanded }
                        )
                    },
                    placeholder = {
                        Text(
                            modifier = Modifier.alpha(alpha = 0.5f),
                            text = "Problema"
                        ) },
                    readOnly = true
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                ) {
                    options.forEach { label ->
                        DropdownMenuItem(onClick = {
                            selectedOption = label
                            expanded = false
                        },
                            text = { Text(text = label) })
                    }
                }
            }
            if (!completed && selectedOption !in options) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Seleccionar un problema", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                value = descriptionText,
                onValueChange = { descriptionText = it },
                placeholder = {
                    Text(
                        modifier = Modifier.alpha(alpha = 0.5f),
                        text = "Descripción"
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    disabledIndicatorColor = Color.Unspecified,
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
            if (!completed && descriptionText.isEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Completar descripción", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Capturar foto (Opcional)",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
            Button(
                onClick = {
                    Toast.makeText(context, "En desarrollo...", Toast.LENGTH_SHORT).show()
                    //if (!hasRequiredPermissions(context)) ActivityCompat.requestPermissions(this, CAMERAX_PERMISSIONS, 0)

                }
            ) {
                Text(text = "Tomar foto")
            }

        }
    }
}

fun validateInformation(
    serviceText: String,
    addressText: String,
    selectedOption: String,
    descriptionText: String
): Boolean {
    return (serviceText.isNotEmpty() && addressText.isNotEmpty() && descriptionText.isNotEmpty() && selectedOption in options)
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        homeUiState = HomeUiState.Success,
        navigateToTakePicture = { },
        onSaveInformation = {}
    )
}