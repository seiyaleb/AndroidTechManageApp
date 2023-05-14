package com.androidtechmanageapp.model

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TechRepository @Inject constructor(private val techDao: TechDao) {

    //全検索処理は公開プロパティとして定義
    val allTechAndURL:Flow<List<TechAndURL>> = techDao.loadAllTechAndURL()

    //カテゴリー別の検索処理はメソッドとして定義
    @WorkerThread
    fun loadByCategoryTechAndURL(category: String):Flow<List<TechAndURL>> {
        return techDao.loadByCategoryTechAndURL(category)
    }

    //追加処理
    @WorkerThread
    suspend fun insertTechAndURL(tech: Tech,url1 :String?,url2 :String?,url3 :String?) {
        techDao.insertTechAndURL(tech,url1,url2,url3)
    }

    //更新処理
    @WorkerThread
    suspend fun updateTechAndURL(tech: Tech,urlList:List<URL>,url1 :String?,url2 :String?,url3 :String?) {
        techDao.updateTechAndURL(tech,urlList,url1,url2,url3)
    }

    //削除処理
    @WorkerThread
    suspend fun deleteTechAndURL(tech: Tech) {
        techDao.deleteTechAndURL(tech)
    }
}