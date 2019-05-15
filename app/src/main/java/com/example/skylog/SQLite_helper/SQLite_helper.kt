package com.example.skylog.SQLite_helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



 class DBHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

override fun onCreate(db:SQLiteDatabase) {
db.execSQL(
    "create table " + TABLE_BBOX + "("
            +KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_year + " integer,"
            + KEY_month + " integer,"
            + KEY_seconds + " integer,"
            + KEY_minute + " integer,"
            + KEY_day + " integer,"
            + KEY_hour + " integer,"

            + KEY_wind + " integer,"
            + KEY_outs6_13 + " integer,"
            + KEY_operatorintervent + " integer,"
            + KEY_instal + " integer,"
            + KEY_ugaz + " real,"
            + KEY_sugaz + " integer,"
            + KEY_linkont + " integer,"
            + KEY_kratzapas + " integer,"
            + KEY_zeroput + " integer,"
            + KEY_s + " real,"
            + KEY_q + " real,"
            + KEY_r + " real,"
            + KEY_h + " real,"
            + KEY_qm + " real,"
            + KEY_m + " integer,"
            + KEY_outs1_5 + " integer,"
            + KEY_dpm_s + " integer,"
            + KEY_dpm_az + " integer,"
            + KEY_dpm_r + " integer,"
            + KEY_dpm_h + " integer,"
            + KEY_force_datx + " integer"
            + ")"
)
}

override fun onUpgrade(db:SQLiteDatabase, oldVersion:Int, newVersion:Int) {
db.execSQL("drop table if exists $TABLE_BBOX")

onCreate(db)

}

companion object {

 val DATABASE_VERSION = 4
 val DATABASE_NAME = "bboxDb"
 val TABLE_BBOX = "bbox"

   val KEY_ID		      = "ID"
   val KEY_year		      = "year"
   val KEY_month		  = "month"
   val KEY_seconds		  = "seconds"
   val KEY_minute		  = "minute"
   val KEY_day		      = "day"
   val KEY_hour		      = "hour"

    val KEY_wind		      = "wind"
    val KEY_outs6_13	      = "outs6_13"
    val KEY_operatorintervent = "operatorintervent"
    val KEY_instal            = "instal"
    val KEY_ugaz		      = "ugaz"
    val KEY_sugaz	          = "sugaz"
    val KEY_linkont	          = "linkont"
    val KEY_kratzapas         = "kratzapas"
    val KEY_zeroput	          = "zeroput"
    val KEY_s		          = "s"
    val KEY_q		          = "q"
    val KEY_r		          = "r"
    val KEY_h                 = "h"
    val KEY_qm                = "qm"
    val KEY_m                 = "m"
    val KEY_outs1_5           = "outs1_5"
    val KEY_dpm_s  	           = "dpm_s"
    val KEY_dpm_az            = "dpm_az"
    val KEY_dpm_r             ="dpm_r"
    val KEY_dpm_h             = "dpm_h"
    val KEY_force_datx        = "force_datx"
}
}