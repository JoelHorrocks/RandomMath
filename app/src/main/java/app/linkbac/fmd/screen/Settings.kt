package app.linkbac.fmd.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ConnectWithoutContact
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.EmojiSymbols
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.ShapeLine
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.linkbac.fmd.vm.SettingsViewModel

@Composable
fun Settings(settingsViewModel: SettingsViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .verticalScroll(scrollState)
    ) {
        Row(modifier = Modifier
            .padding(vertical = 16.dp)) {
            Text(
                "Settings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                "General",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
        SettingsSwitch(
            name = "Daily notifications",
            desc = "Receive daily reminders to practice",
            icon = Icons.Filled.NotificationsActive,
            iconDesc = "desc",
            state = settingsViewModel.notificationsEnabled.collectAsState(),
        ) {
            //settingsViewModel.saveLoadingMessages(it, context)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                "About",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
        SettingsItem(
            name = "Version",
            desc = "1.0.0",
            icon = Icons.Filled.Code,
            iconDesc = "desc"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                "Open source licenses",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
        SettingsItem(
            name = "Noto Emoji",
            desc = "Apache-2.0, Google",
            icon = Icons.Filled.EmojiSymbols,
            iconDesc = "desc"
        )
        SettingsItem(
            name = "Twemoji",
            desc = "CC-BY 4.0, Copyright 2020 Twitter, Inc and other contributors",
            icon = Icons.Filled.EmojiEmotions,
            iconDesc = "desc"
        )
        SettingsItem(
            name = "Konfetti",
            desc = "ISC license, @DanielMartinus",
            icon = Icons.Filled.Celebration,
            iconDesc = "desc"
        )
        SettingsItem(
            name = "ReTeX",
            desc = "GPL-2.0, Geogebra",
            icon = Icons.Filled.ShapeLine,
            iconDesc = "desc"
        )
        SettingsItem(
            name = "JLaTeXMath",
            desc = "GPL-2.0, @opencollab",
            icon = Icons.Filled.Numbers,
            iconDesc = "desc"
        )
        SettingsItem(
            name = "Ktor",
            desc = "Apache-2.0, JetBrains",
            icon = Icons.Filled.ConnectWithoutContact,
            iconDesc = "desc"
        )
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSwitch(name: String, desc: String, icon: ImageVector, iconDesc: String, state: State<Boolean>, onClick: (Boolean) -> Unit) {
    Column(modifier = Modifier
        .toggleable(
            value = state.value,
            role = Role.Switch,
            onValueChange = onClick
        )) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = iconDesc,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1.0f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
                Text(
                    text = desc
                )
            }
            Switch(
                checked = state.value,
                onCheckedChange = null
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(1.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    }
}

@Composable
fun SettingsItem(name: String, desc: String, icon: ImageVector, iconDesc: String) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = iconDesc,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1.0f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
                Text(
                    text = desc
                )
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(1.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    }
}