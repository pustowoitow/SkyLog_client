package com.example.skylog.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.skylog.MQTT_helper.MqttHelper
import com.example.skylog.R
import com.example.skylog.data.My_MQTT_info
import kotlinx.coroutines.*
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlin.coroutines.CoroutineContext
import android.content.Context
import android.content.pm.ActivityInfo
import com.example.skylog.data.Bbox_line
import java.util.ArrayList
import android.util.Base64
import android.widget.TextView
import com.example.skylog.data.parse_helper


class MainPage : Fragment(), CoroutineScope {

    var my_context: Context?=null
    var mqttHelper:MqttHelper?=null
    var Rad:TextView?=null
    var M:TextView?=null
    var Q:TextView?=null
    var H:TextView?=null
    var S:TextView?=null
    var wind:TextView?=null

    internal var BboxList: MutableList<Bbox_line> = ArrayList<Bbox_line>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        /*getActivity()!!.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        getActivity()!!.setRequestedOrientation(ActivityInfo.SC);**/
        var RootView:View=inflater.inflate(R.layout.fragment4_main_page, container, false)
        Rad = RootView.findViewById<TextView>(R.id.R)
        M = RootView.findViewById<TextView>(R.id.M)
        Q = RootView.findViewById<TextView>(R.id.Q)
        H = RootView.findViewById<TextView>(R.id.H)
        S = RootView.findViewById<TextView>(R.id.S)
        wind = RootView.findViewById<TextView>(R.id.wind)
        my_context=RootView.context
        // инициализация базы данных
        //read_database_at_start()
        //Mqtt_load_bbox()
        //setUpRecyclerView()
        //StartOnlineWork()
        return RootView
    }

    override fun onResume() {
        getActivity()!!.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        getActivity()!!.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR)
        super.onResume()
    }

    override fun onPause() {
        getActivity()!!.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)
        getActivity()!!.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
        super.onPause()
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
        val Helper= parse_helper()
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
        Bbox_line.s=Helper.parse_f5toint(encodeValue[8],encodeValue[9])//(encodeValue[8].toInt()+ encodeValue[9].toInt().shl(8)).toFloat()-0.5F)/32F
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

        Rad!!.setText(String.format("%.2f", Bbox_line.r)+" м")
        M!!.setText(Bbox_line.m.toString()+" %")
        Q!!.setText(String.format("%.2f", Bbox_line.q)+" т")
        H!!.setText(String.format("%.2f", Bbox_line.h)+" м")
        S!!.setText(String.format("%.2f", Bbox_line.s)+" м")
        wind!!.setText(Bbox_line.wind.toString()+" м/с")

    }

    //-------Работа с сетью---------------------------------------------

    private val rootJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob


    private fun StartOnlineWork() = launch {
        Mqtt_load_bbox()
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
        rootJob.cancel()
        super.onStop()
    }


    override fun onDestroy() {
        super.onDestroy()
    }

}