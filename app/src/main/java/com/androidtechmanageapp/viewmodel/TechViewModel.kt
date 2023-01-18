package com.androidtechmanageapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.androidtechmanageapp.model.Tech
import com.androidtechmanageapp.model.TechAndURL
import com.androidtechmanageapp.model.TechRepository
import com.androidtechmanageapp.model.URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TechViewModel @Inject constructor(
    private val repository: TechRepository) :ViewModel() {

    //全検索処理はLiveData型の公開プロパティ（初回用）とメソッド（メニュータップ用）として定義
    val allTechAndURL :LiveData<List<TechAndURL>> = repository.allTechAndURL.asLiveData()
    fun loadAllTechAndURL(): LiveData<List<TechAndURL>> {
        return repository.allTechAndURL.asLiveData()
    }

    //カテゴリー別の検索処理（LiveData型）
    fun loadByCategoryTechAndURL(category: String): LiveData<List<TechAndURL>> {
        return repository.loadByCategoryTechAndURL(category).asLiveData()
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
}