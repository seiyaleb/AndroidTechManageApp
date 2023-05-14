package com.androidtechmanageapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidtechmanageapp.model.Tech
import com.androidtechmanageapp.model.TechAndURL
import com.androidtechmanageapp.model.TechRepository
import com.androidtechmanageapp.model.URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TechViewModel @Inject constructor(
    private val repository: TechRepository
    ) :ViewModel() {

    //非同期処理による状態変化の監視をするため、リストのフィルタリングをStateFlowで定義
    private val _filteredTechAndURL = MutableStateFlow<List<TechAndURL>>(listOf())
    val filteredTechAndURL: StateFlow<List<TechAndURL>> = _filteredTechAndURL

    //選択したアイテムのデータ用
    private val _selectedTechAndUrl = MutableLiveData<TechAndURL?>()
    val selectedTechAndUrl: LiveData<TechAndURL?> get() = _selectedTechAndUrl

    init {
        //初期値に全検索データを設定
        loadAllTechAndURL()
    }

    //全検索(Flow→StateFlowに変換)
    fun loadAllTechAndURL() {
        viewModelScope.launch {
            repository.allTechAndURL
                .catch {   Log.e("ERROR","error:loadAllTechAndURL()") }
                .collect { items -> _filteredTechAndURL.value = items }
        }
    }

    //カテゴリー別の検索処理(Flow→StateFlowに変換)
    fun loadByCategoryTechAndURL(category: String) {
        viewModelScope.launch {
            repository.loadByCategoryTechAndURL(category)
                .catch {   Log.e("ERROR","error:loadByCategoryTechAndURL()") }
                .collect { items -> _filteredTechAndURL.value = items }
        }
    }

    //追加処理
    fun insertTechAndURL(tech: Tech, url1 :String?,url2 :String?,url3 :String?) = viewModelScope.launch{
        repository.insertTechAndURL(tech,url1,url2,url3)
    }

    //更新処理
    fun updateTechAndURL(tech: Tech, urlList:List<URL>,url1 :String?,url2 :String?,url3 :String?) = viewModelScope.launch{
        repository.updateTechAndURL(tech,urlList,url1,url2,url3)
    }

    //削除処理
    fun deleteTech(tech: Tech) = viewModelScope.launch{
        repository.deleteTechAndURL(tech)
    }

    //トップ画面のリストで選択したアイテムを保持
    fun selectTechAndUrl(techAndUrl: TechAndURL) {
        _selectedTechAndUrl.value = techAndUrl
    }

    //トップ画面のリストで選択したアイテムを消去
    fun clearSelectedTechAndUrl() {
        _selectedTechAndUrl.value = null
    }
}