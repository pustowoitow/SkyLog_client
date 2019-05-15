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
import android.util.Base64




class bbox : Fragment(), CoroutineScope {

    var my_context: Context?=null
    var dbHelper: DBHelper? = null
    var mqttHelper:MqttHelper?=null
    var database:SQLiteDatabase?=null


    var scrollX:Int = 0

    internal var BboxList: MutableList<Bbox_line> = ArrayList<Bbox_line>()

    internal var rvBbox: RecyclerView?= null

    var headerScroll: HorizontalScrollView?= null

    internal var searchView: SearchView?= null

    internal var BboxAdapter: BboxAdapter?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        var RootView:View=inflater.inflate(R.layout.fragment2_bbox, container, false)
        rvBbox = RootView.findViewById<RecyclerView>(R.id.rvClub)
        headerScroll = RootView.findViewById<HorizontalScrollView>(R.id.headerScroll)
        my_context=RootView.context
        rvBbox!!.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                scrollX = scrollX+ dx

                headerScroll!!.scrollTo(scrollX, 0)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
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
        val string=mqttMessage.payload.toString()
        val encodeValue = Base64.decode(mqttMessage.payload, Base64.DEFAULT)
        var Bbox_line:Bbox_line=Bbox_line(0)
        Bbox_line.year=2000+encodeValue[0].toInt().and(0x3F)
        Bbox_line.month=encodeValue[0].toInt().shr(6).and(0x03).shl(2)+encodeValue[1].toInt().shr(6).and(0x03)
        Bbox_line.seconds=encodeValue[1].toInt().and(0x3F)
        Bbox_line.minute=encodeValue[2].toInt().and(0x3F)
        Bbox_line.day=encodeValue[2].toInt().shr(6).and(0x03).shl(2)+encodeValue[3].toInt().shr(5).and(0x07)
        Bbox_line.hour=encodeValue[3].toInt().and(0x1F)

        Bbox_line.wind=encodeValue[4].toInt().and(0x3F)
        Bbox_line.outs6_13= 1111111//encodeValue[0].toInt().shr(6).and(0x0F)
        Bbox_line.operatorintervent=if(encodeValue[5].toInt().shr(6).and(0x01)==1) true else false
        Bbox_line.instal=if(encodeValue[5].toInt().shr(7).and(0x01)==1) true else false
        Bbox_line.ugaz= (encodeValue[6].toInt()+ encodeValue[7].toInt().and(0x07).shl(8)).toFloat()  //&&&&
        Bbox_line.sugaz= if (encodeValue[7].toInt().shr(3).and(0x01)==1) true else false
        Bbox_line.linkont=if (encodeValue[7].toInt().shr(5).and(0x01)==1) true else false
        Bbox_line.kratzapas=if (encodeValue[7].toInt().shr(6).and(0x01)==1) true else false
        Bbox_line.zeroput=if (encodeValue[7].toInt().shr(7).and(0x01)==1) true else false
        Bbox_line.s=((encodeValue[8].toInt()+ encodeValue[9].toInt().shl(8)).toFloat()-0.5F)/32F
        Bbox_line.q=((encodeValue[10].toInt()+ encodeValue[11].toInt().shl(8)).toFloat()-0.5F)/256F
        Bbox_line.r=((encodeValue[12].toInt()+ encodeValue[13].toInt().shl(8)).toFloat()-0.5F)/256F
        Bbox_line.h=((encodeValue[14].toInt()+ encodeValue[15].toInt().shl(8)).toFloat()-0.5F)/32F
        Bbox_line.qm=((encodeValue[16].toInt()+ encodeValue[17].toInt().shl(8)).toFloat()-0.5F)/256F
        Bbox_line.m=encodeValue[18].toInt()
        Bbox_line.outs1_5=1
        Bbox_line.dpm_s=encodeValue[20].toInt()+ encodeValue[21].toInt().shl(8)
        Bbox_line.dpm_az=encodeValue[22].toInt()+ encodeValue[23].toInt().shl(8)
        Bbox_line.dpm_r=encodeValue[24].toInt()+ encodeValue[25].toInt().shl(8)
        Bbox_line.dpm_h=encodeValue[26].toInt()+ encodeValue[27].toInt().shl(8)
        Bbox_line.force_dat=encodeValue[28].toInt()+ encodeValue[29].toInt().shl(8)

        var contentValues = ContentValues()
        contentValues.put(DBHelper.KEY_year,               Bbox_line.year);
        contentValues.put(DBHelper.KEY_month,               Bbox_line.month);
        contentValues.put(DBHelper.KEY_seconds,               Bbox_line.seconds);
        contentValues.put(DBHelper.KEY_minute,               Bbox_line.minute);
        contentValues.put(DBHelper.KEY_day,               Bbox_line.day);
        contentValues.put(DBHelper.KEY_hour,               Bbox_line.hour);

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
        database!!.insert(DBHelper.TABLE_BBOX, null, contentValues);
        //Получение присвоенного ID
        val cursor = database!!.query(DBHelper.TABLE_BBOX, null, null, null, null, null, null)
        cursor.moveToLast()
        Bbox_line.ID=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID))
        cursor.close()
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

        do
        {
            Bbox_line.ID=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID))
            Bbox_line.year=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_year))
            Bbox_line.month=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_month))
            Bbox_line.seconds=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_seconds))
            Bbox_line.minute=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_minute))
            Bbox_line.day=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_day))
            Bbox_line.hour=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_hour))

            Bbox_line.wind=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_wind))
            Bbox_line.outs6_13=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_outs6_13))
            Bbox_line.operatorintervent=if (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_operatorintervent))==0) false else true
            Bbox_line.instal=if (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_instal))==0) false else true
            Bbox_line.ugaz= cursor.getFloat(cursor.getColumnIndex(DBHelper.KEY_ugaz))
            Bbox_line.sugaz=if (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_sugaz))==0) false else true
            Bbox_line.linkont=if (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_linkont))==0) false else true
            Bbox_line.kratzapas=if (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_kratzapas))==0) false else true
            Bbox_line.zeroput=if (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_zeroput))==0) false else true
            Bbox_line.s=cursor.getFloat(cursor.getColumnIndex(DBHelper.KEY_s))
            Bbox_line.q=cursor.getFloat(cursor.getColumnIndex(DBHelper.KEY_q))
            Bbox_line.r=cursor.getFloat(cursor.getColumnIndex(DBHelper.KEY_r))
            Bbox_line.h=cursor.getFloat(cursor.getColumnIndex(DBHelper.KEY_h))
            Bbox_line.qm=cursor.getFloat(cursor.getColumnIndex(DBHelper.KEY_qm))
            Bbox_line.m=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_m))
            Bbox_line.outs1_5=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_outs1_5))
            Bbox_line.dpm_s=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_dpm_s))
            Bbox_line.dpm_az=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_dpm_az))
            Bbox_line.dpm_r=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_dpm_r))
            Bbox_line.dpm_h=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_dpm_h))
            Bbox_line.force_dat=cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_force_datx))
            BboxList.add(Bbox_line)
        }while(cursor.moveToNext())

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

            delay(1000)
        }
    }
//---------------------------------------------------------------------------------------------------------------------------------
    override fun onStop() {
        val cmd: String = My_MQTT_info.ChoosenBlock + My_MQTT_info.cmd.cmd_online
        mqttHelper!!.publishMessage("state=0,period=1",0,cmd)  //посылаем неизвестyю команду для получения ответа
    database!!.delete(DBHelper.TABLE_BBOX, null, null)
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