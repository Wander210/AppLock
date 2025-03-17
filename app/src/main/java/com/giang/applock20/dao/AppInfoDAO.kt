package com.giang.applock20.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.giang.applock20.model.AppInfo

interface AppInfoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppInfo(appInfo: AppInfo) : Long

    @Update
    suspend fun updateAppInfo(appInfo: AppInfo) : Int

    @Delete
    suspend fun deleteAppInfo(appInfo: AppInfo) : Int

    @Query("DELETE FROM appInfo_data_tab")
    suspend fun deleteAll()

    @Query("SELECT * FROM appInfo_data_tab WHERE appInfo_isLocked = 0")
    suspend fun getAllApp(): ArrayList<AppInfo>

    @Query("SELECT * FROM appInfo_data_tab WHERE appInfo_isLocked = 1")
    suspend fun getLockedApp(): ArrayList<AppInfo>
}