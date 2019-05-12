package com.example.skylog.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.skylog.MQTT_helper.MqttHelper
import com.example.skylog.R
import com.example.skylog.SQLite_helper.DBHelper
import com.example.skylog.data.My_MQTT_info
import kotlinx.coroutines.*
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlin.coroutines.CoroutineContext
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.widget.HorizontalScrollView
import com.example.skylog.adapter.BboxAdapter
import com.example.skylog.data.Bbox_line
import com.example.skylog.utils.FixedGridLayoutManager
import java.util.ArrayList
import kotlin.experimental.and


class bbox : Fragment(), CoroutineScope {

    var my_context: Context?=null
    var dbHelper: DBHelper? = null
    var mqttHelper:MqttHelper?=null
    var database:SQLiteDatabase?=null


    var scrollX:Int = 0

    internal var BboxList: MutableList<Bbox_line> = ArrayList<Bbox_line>()

    internal var rvBbox: RecyclerView?= null

    internal var headerScroll: HorizontalScrollView?= null

    internal var searchView: SearchView?= null

    internal var BboxAdapter: BboxAdapter?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        var RootView:View=inflater.inflate(R.layout.fragment2_bbox, container, false)
        rvBbox = RootView.findViewById<RecyclerView>(R.id.rvClub)
        headerScroll = RootView.findViewById<HorizontalScrollView>(R.id.headerScroll)
        my_context=RootView.context
        // инициализация базы данных
        //read_database_at_start()
        //Mqtt_load_bbox()
        //setUpRecyclerView()
        //StartOnlineWork()
        return RootView
    }

    override fun onStart() {
        super.onStart()
        StartOnlineWork()
    }

    /*rvBbox.addOnScrollListener(object recyclerView: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            scrollX = scrollX+ dx

            headerScroll.scrollTo(scrollX, 0)
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }
    })*/


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //you can set the title for your toolbar here for different fragments different titles
    }

    private fun Mqtt_load_bbox() {
        mqttHelper = MqttHelper(context!!)//(getActivity()!!.getApplicationContext())
        mqttHelper!!.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                My_MQTT_info.Connection_status=true
            }

            override fun connectionLost(throwable: Throwable) {
               // Snackbar.make(email, "Lost", Snackbar.LENGTH_INDEFINITE).show()
                My_MQTT_info.Connection_status=false
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                Log.w("Debug", mqttMessage.toString())
                if (topic.equals(My_MQTT_info.ChoosenBlock + My_MQTT_info.info.info_tpchr_online)) parse_data(mqttMessage)
            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
                Log.w("Debug", iMqttDeliveryToken.toString())
                //  Snackbar.make(email, iMqttDeliveryToken.toString(), Snackbar.LENGTH_INDEFINITE).show()
                //   My_MQTT_info.token=iMqttDeliveryToken.toString()

            }
        })
    }
// Расшивровка и сохранение данных
    private fun parse_data(mqttMessage:MqttMessage)
    {
        var temp:Byte=mqttMessage.payload[0]
        var temp2:Byte=temp.and(0x0f)
        var Bbox_line:Bbox_line=Bbox_line(0)
        Bbox_line.wind=((mqttMessage.payload[0])&0x3f).toInt()
        Bbox_line.outs6_13=(mqttMessage.payload[0]>>6).toInt()
        Bbox_line.operatorintervent=true
        Bbox_line.instal=true
        Bbox_line.ugaz= 1
        Bbox_line.sugaz= true
        Bbox_line.linkont=true
        Bbox_line.kratzapas=true
        Bbox_line.zeroput=true
        Bbox_line.s=1
        Bbox_line.q=1
        Bbox_line.r=1
        Bbox_line.h=1
        Bbox_line.qm=1
        Bbox_line.m=1
        Bbox_line.outs1_5=1
        Bbox_line.dpm_s=1
        Bbox_line.dpm_az=1
        Bbox_line.dpm_r=1
        Bbox_line.dpm_h=1
        Bbox_line.force_dat=1

        var contentValues = ContentValues()
        contentValues.put(DBHelper.KEY_wind,               Bbox_line.wind);
        contentValues.put(DBHelper.KEY_outs6_13,           Bbox_line.outs6_13);
        contentValues.put(DBHelper.KEY_operatorintervent,  Bbox_line.operatorintervent);
        contentValues.put(DBHelper.KEY_instal,                Bbox_line.instal);
        contentValues.put(DBHelper.KEY_ugaz,                Bbox_line.ugaz);
        contentValues.put(DBHelper.KEY_sugaz,                Bbox_line.sugaz);
        contentValues.put(DBHelper.KEY_linkont,                 Bbox_line.linkont);
        contentValues.put(DBHelper.KEY_kratzapas,                Bbox_line.kratzapas);
        contentValues.put(DBHelper.KEY_zeroput,                Bbox_line.zeroput);
        contentValues.put(DBHelper.KEY_s,                Bbox_line.s);
        contentValues.put(DBHelper.KEY_q,                   Bbox_line.q);
        contentValues.put(DBHelper.KEY_r,          Bbox_line.r);
        contentValues.put(DBHelper.KEY_h,               Bbox_line.h);
        contentValues.put(DBHelper.KEY_qm,                   Bbox_line.qm);
        contentValues.put(DBHelper.KEY_m,          Bbox_line.m);
        contentValues.put(DBHelper.KEY_outs1_5,                   Bbox_line.outs1_5);
        contentValues.put(DBHelper.KEY_dpm_s,                   Bbox_line.s);
        contentValues.put(DBHelper.KEY_dpm_az,                  Bbox_line.dpm_az);
        contentValues.put(DBHelper.KEY_dpm_r,                Bbox_line.dpm_r);
        contentValues.put(DBHelper.KEY_dpm_h,               Bbox_line.dpm_h);
        contentValues.put(DBHelper.KEY_force_datx,                Bbox_line.force_dat);
        BboxList!!.add(Bbox_line)
        database!!.insert(DBHelper.TABLE_BBOX, null, contentValues);
       // BboxAdapter!!.Bbox_List.clear()
        BboxAdapter!!.Bbox_List.add(Bbox_line)
        BboxAdapter!!.notifyDataSetChanged()
    }

// ----Инициализация данных-------------------------------------------------------------------------------------------------------------------------
    suspend private fun read_database_at_start() {
    dbHelper = DBHelper(activity!!.baseContext)//(my_context!!)
    delay(50)
    database=dbHelper!!.getWritableDatabase()
        val cursor = database!!.query(DBHelper.TABLE_BBOX, null, null, null, null, null, null)
    if (cursor.moveToFirst()) {
        BboxList.clear()
        var Bbox_line:Bbox_line=Bbox_line(0)
        //var i:Int=cursor.columnCount

        for (i in 0..cursor.count)
        {

            Bbox_line.wind=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_wind))
            Bbox_line.outs6_13=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_outs6_13))
            Bbox_line.operatorintervent=if (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_operatorintervent))==0) false else true
            Bbox_line.instal=if (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_instal))==0) false else true
            Bbox_line.ugaz= cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ugaz))
            Bbox_line.sugaz=if (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_sugaz))==0) false else true
            Bbox_line.linkont=if (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_linkont))==0) false else true
            Bbox_line.kratzapas=if (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_kratzapas))==0) false else true
            Bbox_line.zeroput=if (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_zeroput))==0) false else true
            Bbox_line.s=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_s))
            Bbox_line.q=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_q))
            Bbox_line.r=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_r))
            Bbox_line.h=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_h))
            Bbox_line.qm=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_qm))
            Bbox_line.m=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_m))
            Bbox_line.outs1_5=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_outs1_5))
            Bbox_line.dpm_s=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_dpm_s))
            Bbox_line.dpm_az=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_dpm_az))
            Bbox_line.dpm_r=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_dpm_r))
            Bbox_line.dpm_h=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_dpm_h))
            Bbox_line.force_dat=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_force_datx))
            BboxList.add(Bbox_line)
            cursor.moveToNext()
        }

    } else
        Log.d("mLog", "0 rows")

    cursor.close()
    }

    //-------Работа с сетью---------------------------------------------

    private val rootJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob


    private fun StartOnlineWork() = launch {
        read_database_at_start()
        Mqtt_load_bbox()
        setUpRecyclerView()
        while(true) {
            if (mqttHelper!!.check_connection()) {
                // запуск режима онлайн с периодичностью в 1с
                val cmd: String = My_MQTT_info.ChoosenBlock + My_MQTT_info.cmd.cmd_online
                val topic: String = My_MQTT_info.ChoosenBlock + My_MQTT_info.info.info_tpchr_online
                mqttHelper!!.publishMessage("state=1,period=1",0,cmd)  //посылаем неизвестyю команду для получения ответа
                mqttHelper!!.subscribeToTopic(topic)
                break;
                //database
            }

            delay(5000)
        }
    }
//---------------------------------------------------------------------------------------------------------------------------------
    override fun onStop() {
        val cmd: String = My_MQTT_info.ChoosenBlock + My_MQTT_info.cmd.cmd_online
        mqttHelper!!.publishMessage("state=0,period=1",0,cmd)  //посылаем неизвестyю команду для получения ответа
        rootJob.cancel()
        dbHelper!!.close()
        super.onStop()
    }


    override fun onDestroy() {
        super.onDestroy()
    }


    private fun setUpRecyclerView() {
        BboxAdapter = BboxAdapter(my_context!!, BboxList)

        val manager = FixedGridLayoutManager()
        //manager.setTotalColumnCount(1)
        rvBbox!!.setLayoutManager(manager)
        rvBbox!!.setAdapter(BboxAdapter)
        rvBbox!!.addItemDecoration(DividerItemDecoration(my_context, DividerItemDecoration.VERTICAL))
    }
}