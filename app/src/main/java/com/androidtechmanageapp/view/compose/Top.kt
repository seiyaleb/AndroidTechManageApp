package com.androidtechmanageapp.view.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androidtechmanageapp.R
import com.androidtechmanageapp.model.TechAndURL
import com.androidtechmanageapp.viewmodel.TechViewModel

//トップ画面_全体
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TopScreen(
    onNavigateToInsert: () -> Unit,
    onNavigateToDetail: () -> Unit,
    viewModel: TechViewModel
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier.testTag("TopScreen"),
        scaffoldState = scaffoldState,
        topBar = {
            TopToolBar(viewModel)
        },
        content = {
            TopMainContent(viewModel,onNavigateToInsert,onNavigateToDetail)
        }
    )
}

//ツールバー_全体
@Composable
fun TopToolBar(viewModel: TechViewModel) {

    val menuExpanded= remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        actions = {

            //カテゴリーメニューを開くトリガーとなるオプション
            IconButton(onClick = { menuExpanded.value = true }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.menu_categories)
                )
            }

            //検索のカテゴリーメニューを表示
            TopToolBarMenu(viewModel,menuExpanded)
        }
    )
}

//ツールバー_メニュー部分
@Composable
fun TopToolBarMenu(
    viewModel: TechViewModel,
    menuExpanded: MutableState<Boolean>
) {

    androidx.compose.material.DropdownMenu(
        expanded = menuExpanded.value,
        onDismissRequest = { menuExpanded.value = false }
    ) {
        DropdownMenuItem(
            onClick = {
                viewModel.loadAllTechAndURL()
                menuExpanded.value = false
            }) {
            Text(stringResource(R.string.menu_all))
        }

        DropdownMenuItem(
            onClick = {
                viewModel.loadByCategoryTechAndURL("Basic")
                menuExpanded.value = false
            }) {
            Text(stringResource(R.string.menu_category1))
        }

        DropdownMenuItem(
            onClick = {
                viewModel.loadByCategoryTechAndURL("Architecture")
                menuExpanded.value = false
            }) {
            Text(stringResource(R.string.menu_category2))
        }

        DropdownMenuItem(
            onClick = {
                viewModel.loadByCategoryTechAndURL("Library")
                menuExpanded.value = false
            }) {
            Text(stringResource(R.string.menu_category3))
        }

        DropdownMenuItem(
            onClick = {
                viewModel.loadByCategoryTechAndURL("Test")
                menuExpanded.value = false
            }) {
            Text(stringResource(R.string.menu_category4))
        }

        DropdownMenuItem(
            onClick = {
                viewModel.loadByCategoryTechAndURL("Else")
                menuExpanded.value = false
            }) {
            Text(stringResource(R.string.menu_category5))
        }
    }
}

//メインコンテンツ_リスト表示
@Composable
fun TopMainContent(
    viewModel: TechViewModel,
    onNavigateToInsert: () -> Unit,
    onNavigateToDetail: () -> Unit
) {
    //選択されたメニューに基づいてデータを更新する状態
    //初期では全てのデータを表示
    val techAndUrlList by viewModel.filteredTechAndURL.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        //習得技術をリスト表示
        LazyColumn {
            items(techAndUrlList) {
                TechItem(it,viewModel) { onNavigateToDetail() }
            }
        }

        //FABボタンで追加画面へ
        FloatingActionButton(
            onClick = { onNavigateToInsert() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
                .navigationBarsPadding()
                .testTag("FAB")
        ) {
            Icon(Icons.Filled.Add, contentDescription = "追加")
        }
    }
}

//リストの各アイテム
@Composable
fun TechItem(
    techAndUrl: TechAndURL,
    viewModel: TechViewModel,
    onNavigateToDetail: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("item")
            .clickable {
                viewModel.selectTechAndUrl(techAndUrl)
                onNavigateToDetail()
            }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp,
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(
                text = techAndUrl.tech.title,
                modifier = Modifier
                    .height(48.dp)
                    .padding(start = 10.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}