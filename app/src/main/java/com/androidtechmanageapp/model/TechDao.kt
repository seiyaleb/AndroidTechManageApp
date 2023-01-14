package com.androidtechmanageapp.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TechDao {
    @Transaction
    @Query("SELECT * FROM tech")
    fun loadAllTechAndURL(): Flow<List<TechAndURL>>

    @Transaction
    @Query("SELECT * FROM tech where category = :category")
    fun loadByCategoryTechAndURL(category: String): Flow<List<TechAndURL>>

    @Transaction
    suspend fun insertTechAndURL(tech: Tech,_url1 :String?,_url2 :String?,_url3 :String?) {
        //技術テーブルに追加
        val techId = insertTech(tech).toInt()
        //重複データの場合、ここでトランザクションを中止し、ロールバック
        //技術テーブルに追加したレコードのIDを元に、URLテーブルに追加
        val url1 = URL(tech_id = techId, url = _url1 ?: "")
        val url2 = URL(tech_id = techId, url = _url2 ?: "")
        val url3 = URL(tech_id = techId, url = _url3 ?: "")
        insertURL(url1)
        insertURL(url2)
        insertURL(url3)
    }

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTech(tech: Tech):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertURL(url: URL)

    @Transaction
    suspend fun updateTechAndURL(tech: Tech,urlList:List<URL>,url1 :String?,url2 :String?,url3 :String?) {
        //技術テーブルを更新
        updateTech(tech)
        //URLテーブルを更新
        urlList[0].url = url1 ?: ""
        urlList[1].url = url2 ?: ""
        urlList[2].url = url3 ?: ""
        updateURL(urlList[0])
        updateURL(urlList[1])
        updateURL(urlList[2])
    }

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateTech(tech:Tech)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateURL(url: URL)

    @Delete
    suspend fun deleteTechAndURL(vararg tech: Tech)
}