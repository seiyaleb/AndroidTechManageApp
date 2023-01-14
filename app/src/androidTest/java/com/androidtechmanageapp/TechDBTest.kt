package com.androidtechmanageapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.androidtechmanageapp.model.Tech
import com.androidtechmanageapp.model.TechDao
import com.androidtechmanageapp.model.TechRoomDatabase
import com.androidtechmanageapp.model.URL
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

//Roomを用いたDB操作の単体テスト→全て合格
@RunWith(AndroidJUnit4::class)
class TechDBTest {

    private lateinit var techDao: TechDao
    private lateinit var db: TechRoomDatabase

    @Before
    fun setupDatabase() {
        //DBを初期化
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TechRoomDatabase::class.java
        ).allowMainThreadQueries().build()
        techDao = db.techDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        //毎回DBを閉じる
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun insertTechURL() = runBlocking {

        //DBに追加した特定のデータが適切に保存されているか検証
        val tech = Tech(1,"Room","DB操作","Library")
        techDao.insertTechAndURL(tech,"test1","test2",null)

        val allTechAndURL = techDao.loadAllTechAndURL().first()

        assertEquals(allTechAndURL[0].tech.title,tech.title)
        assertEquals(allTechAndURL[0].urls[0].url,"test1")
        assertEquals(allTechAndURL[0].urls[1].url,"test2")
        assertEquals(allTechAndURL[0].urls[2].url,"")
    }

    @Test
    @Throws(IOException::class)
    fun getAllTechURL() = runBlocking {

        //DBに追加した全データが適切に保存されているか検証
        val tech1 = Tech(1,"Room","DB操作","Library")
        val tech2 = Tech(2,"Retrofit2","API通信","Library")
        techDao.insertTechAndURL(tech1,"test1",null,null)
        techDao.insertTechAndURL(tech2,"testTest1",null,null)

        val allTechAndURL = techDao.loadAllTechAndURL().first()

        assertEquals(allTechAndURL[0].tech.title,tech1.title)
        assertEquals(allTechAndURL[1].tech.title,tech2.title)
        assertEquals(allTechAndURL[0].urls[0].url,"test1")
        assertEquals(allTechAndURL[1].urls[0].url,"testTest1")
    }

    @Test
    @Throws(IOException::class)
    fun getByCategoryTechURL() = runBlocking {

        //DBに追加したデータがカテゴリー別に適切な読み込みをするか検証
        val tech = Tech(1,"Room","DB操作","Library")
        techDao.insertTechAndURL(tech,"test1",null,null)

        val allByLibrary = techDao.loadByCategoryTechAndURL("Library").first()

        assertEquals(allByLibrary[0].tech.title,tech.title)
        assertEquals(allByLibrary[0].urls[0].url,"test1")
    }

    @Test
    @Throws(IOException::class)
    fun updateTechURL() = runBlocking {

        //DBに追加した特定のデータが適切に更新されるか検証
        val tech = Tech(1,"Room","DB操作","Library")
        techDao.insertTechAndURL(tech,"test1",null,null)

        val updateTech = Tech(1,"Retrofit","API通信","Library")
        val urlList = listOf(URL(1,1,"test1"),URL(2,1,null),URL(3,1,null))
        techDao.updateTechAndURL(updateTech,urlList,"test1test1","test2test2","test3test3")

        val allTechAndURL = techDao.loadAllTechAndURL().first()

        assertEquals(allTechAndURL[0].tech.title,updateTech.title)
        assertEquals(allTechAndURL[0].urls[0].url,"test1test1")
        assertEquals(allTechAndURL[0].urls[1].url,"test2test2")
        assertEquals(allTechAndURL[0].urls[2].url,"test3test3")
    }

    @Test
    @Throws(IOException::class)
    fun deleteTechURL() = runBlocking {

        //DBに追加した特定のデータが適切に削除されるか検証
        val tech = Tech(1,"Room","DB操作","Library")
        techDao.insertTechAndURL(tech,"test1",null,null)

        techDao.deleteTechAndURL(tech)

        val allTechAndURL = techDao.loadAllTechAndURL().first()

        assertEquals(0,allTechAndURL.size)
    }
}