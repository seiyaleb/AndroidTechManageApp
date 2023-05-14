package com.androidtechmanageapp.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidtechmanageapp.view.compose.NavigationTop
import com.androidtechmanageapp.viewmodel.TechViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: TechViewModel = hiltViewModel()
            NavigationTop(viewModel)
        }
    }
}