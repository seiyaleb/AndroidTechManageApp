package com.androidtechmanageapp.view.compose

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.androidtechmanageapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//追加画面と詳細画面の共通処理

//カテゴリーを選択するドロップメニュー
@Composable
fun DropdownMenu(
    initialValue: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val items = context.resources.getStringArray(R.array.category_array).toList()
    var selectedIndex by remember { mutableStateOf(items.indexOf(initialValue).takeIf { it >= 0 } ?: 0)}

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(
                BorderStroke(1.dp, androidx.compose.ui.graphics.Color.LightGray),
                RoundedCornerShape(4.dp)
            )
            .clickable { expanded = true },
        contentAlignment = Alignment.Center
    ) {
        Text(
            items[selectedIndex],
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(10.dp)
        )
        Icon(
            Icons.Filled.ArrowDropDown, "contentDescription",
            Modifier.align(Alignment.CenterEnd)
        )
        androidx.compose.material.DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                    onItemSelected(item)
                }) {
                    Text(text = item)
                }
            }
        }
    }
}

//スナックバー表示
fun showSnackBar(
    context: Context,
    coroutineScope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    flag : Int
) {
    coroutineScope.launch {

        when(flag) {
            0 -> snackBarHostState.showSnackbar(context.getString(R.string.snackbar_input_warning))
            2 -> snackBarHostState.showSnackbar(context.getString(R.string.snackbar_update))
        }
    }
}





