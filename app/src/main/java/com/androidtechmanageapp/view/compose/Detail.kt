package com.androidtechmanageapp.view.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androidtechmanageapp.R
import com.androidtechmanageapp.model.TechAndURL
import com.androidtechmanageapp.viewmodel.TechViewModel

//詳細画面_全体
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    onNavigateToTop: () -> Unit,
    viewModel: TechViewModel
) {
    val techAndUrl by viewModel.selectedTechAndUrl.observeAsState(null)
    val scaffoldState = rememberScaffoldState()
    val openDialog = remember { mutableStateOf(false) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { DetailToolBar(openDialog)},
        content = {
            techAndUrl?.let { it1 ->
                DetailMainContent(
                    viewModel,
                    it1
                )
            }
        }
    )

    if (openDialog.value) {
        techAndUrl?.let {
            DeleteDialog(
                openDialog,onNavigateToTop,viewModel, it
            )
        }
    }

    //トップ画面から共有データをクリア
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectedTechAndUrl()
        }
    }
}

//ツールバー_全体
@Composable
fun DetailToolBar(openDialog: MutableState<Boolean>) {
    TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        actions = {
            IconButton(
                modifier = Modifier.testTag("delete"),
                onClick = { openDialog.value = true }
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.menu_delete)
                )
            }
        }
    )
}

//メインコンテンツ_データ更新
@Composable
fun DetailMainContent(
    viewModel: TechViewModel,
    techAndUrl: TechAndURL
) {
    val (selectedItem, setSelectedItem) = remember { mutableStateOf("") }
    val snackBarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    //入力欄の初期値にトップ画面からViewModel経由で共有されたデータを表示
    val textStateName = remember {
        mutableStateOf(TextFieldValue(techAndUrl.tech.title ?: ""))
    }
    val textStateDetail = remember {
        mutableStateOf(TextFieldValue(techAndUrl.tech.detail ?: ""))
    }
    val textStateUrl1 = remember {
        mutableStateOf(TextFieldValue(techAndUrl.urls[0].url ?: ""))
    }
    val textStateUrl2 = remember {
        mutableStateOf(TextFieldValue(techAndUrl.urls[1].url ?: ""))
    }
    val textStateUrl3 = remember {
        mutableStateOf(TextFieldValue(techAndUrl.urls[2].url ?: ""))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {

        //技術名を入力
        TextField(
            value = textStateName.value,
            onValueChange = {textStateName.value = it},
            label = { Text(text = stringResource(R.string.et_label_title)) },
            textStyle = TextStyle(fontSize = 18.sp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        //カテゴリーを選択
        techAndUrl.tech.category.let {
            DropdownMenu(initialValue = it, onItemSelected = { item ->
                setSelectedItem(item)
            })
        }

        Spacer(modifier = Modifier.height(5.dp))

        //詳細情報を入力
        TextField(
            value = textStateDetail.value,
            onValueChange = { textStateDetail.value = it },
            label = { Text(text = stringResource(R.string.et_label_detail)) },
            textStyle = TextStyle(fontSize = 18.sp),
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(20.dp))

        //URL情報1を入力
        TextField(
            value = textStateUrl1.value,
            onValueChange = { textStateUrl1.value = it },
            label = { Text(text = stringResource(R.string.et_label_url1)) },
            textStyle = TextStyle(fontSize = 18.sp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        //URL情報2を入力
        TextField(
            value = textStateUrl2.value,
            onValueChange = { textStateUrl2.value = it },
            label = { Text(text = stringResource(R.string.et_label_url2)) },
            textStyle = TextStyle(fontSize = 18.sp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        //URL情報3を入力
        TextField(
            value = textStateUrl3.value,
            onValueChange = { textStateUrl3.value = it },
            label = { Text(text = stringResource(R.string.et_label_url3)) },
            textStyle = TextStyle(fontSize = 18.sp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        //更新ボタン
        UpdateBtn {
            onTapUpdateBtn(
                textStateName.value.text,
                textStateDetail.value.text,
                textStateUrl1.value.text,
                textStateUrl2.value.text,
                textStateUrl3.value.text,
                techAndUrl,
                viewModel,
                selectedItem
            ) {
                showSnackBar(
                    context = context,
                    coroutineScope = coroutineScope,
                    snackBarHostState = snackBarHostState,
                    it
                )
            }
        }

        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.padding(top = 150.dp),
            snackbar = {
                Snackbar {
                    Text(text = it.message)
                }
            }
        )
    }
}

//更新ボタン
@Composable
fun UpdateBtn(
    onClick: () -> Unit
) {
    Button(
        onClick = {onClick()},
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Text(
            text = stringResource(R.string.btn_update),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold)
    }
}

//追加ボタンをタップした場合の処理
fun onTapUpdateBtn(
    title: String,
    detail: String,
    url1: String,
    url2: String,
    url3: String,
    techAndUrl: TechAndURL,
    viewModel: TechViewModel,
    selectedItem: String,
    onShowSnackBar: (Int) -> Unit
) {

    // 技術名が入力されていない場合、スナックバー表示
    if (title.isEmpty()) {
        //警告を示すスナックバー表示
        onShowSnackBar(0)

    } else {
        // データ更新
        val tech = techAndUrl.tech
        val urlList = techAndUrl.urls
        tech.title = title
        tech.category = selectedItem
        tech.detail = detail ?: ""
        viewModel.updateTechAndURL(tech,urlList,url1,url2,url3)
        onShowSnackBar(2)
    }
}

//削除ダイアログ
@Composable
fun DeleteDialog(
    openDialog: MutableState<Boolean>,
    onNavigateToTop: () -> Unit,
    viewModel: TechViewModel,
    techAndUrl: TechAndURL
) {
    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    //データ削除
                    techAndUrl.tech.let {
                        viewModel.deleteTech(it)
                        onNavigateToTop()
                    }
                }
            ) {
                Text(stringResource(R.string.dialog_delete_yes))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { openDialog.value = false }
            ) {
                Text(stringResource(R.string.dialog_delete_no))
            }
        },
        title = {
            Text(stringResource(R.string.dialog_delete_title))
        }
    )
}



