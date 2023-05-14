package com.androidtechmanageapp.view.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androidtechmanageapp.R
import com.androidtechmanageapp.model.Tech
import com.androidtechmanageapp.viewmodel.TechViewModel

//追加画面_全体
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun InsertScreen(
    onNavigateToTop: () -> Unit,
    viewModel: TechViewModel
) {

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) }
            )
        },
        content = {
            InsertMainContent(onNavigateToTop,viewModel)
        }
    )
}

//メインコンテンツ_データ入力
@Composable
fun InsertMainContent(
    onNavigateToTop: () -> Unit,
    viewModel: TechViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val (selectedItem, setSelectedItem) = remember { mutableStateOf("") }
    val textStateName = remember { mutableStateOf(TextFieldValue()) }
    val textStateDetail = remember { mutableStateOf(TextFieldValue()) }
    val textStateUrl1 = remember { mutableStateOf(TextFieldValue()) }
    val textStateUrl2 = remember { mutableStateOf(TextFieldValue()) }
    val textStateUrl3 = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        //技術名を入力
        TextField(
            value = textStateName.value,
            onValueChange = {textStateName.value = it},
            label = { Text(text = stringResource(R.string.et_label_title))},
            textStyle = TextStyle(fontSize = 18.sp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        //カテゴリーを選択
        DropdownMenu(initialValue = "", onItemSelected = { item ->
            setSelectedItem(item)
        })

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

        //追加ボタン
        InsertBtn {
            onTapInsertBtn(
                textStateName.value.text,
                textStateDetail.value.text,
                textStateUrl1.value.text,
                textStateUrl2.value.text,
                textStateUrl3.value.text,
                onNavigateToTop,
                viewModel,
                selectedItem
            ) {
                showSnackBar(
                    context = context,
                    coroutineScope = coroutineScope,
                    snackBarHostState = snackBarHostState,
                    flag = 0
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

//追加ボタン
@Composable
fun InsertBtn(
    onClick: () -> Unit
) {
    Button(
        onClick = {onClick()},
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Text(
            text = stringResource(R.string.btn_insert),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold)
    }
}

//追加ボタンをタップした場合の処理
fun onTapInsertBtn(
    title: String,
    detail: String,
    url1: String,
    url2: String,
    url3: String,
    onNavigateToTop: () -> Unit,
    viewModel: TechViewModel,
    selectedItem: String,
    onShowSnackBar: () -> Unit
) {

    // 技術名が入力されていない場合、スナックバー表示
    if (title.isEmpty()) {
        onShowSnackBar()

    } else {
        // データ追加
        val tech = Tech(title = title, detail = detail ?: "", category = selectedItem)
        viewModel.insertTechAndURL(tech, url1, url2, url3)
        onNavigateToTop()
    }
}


