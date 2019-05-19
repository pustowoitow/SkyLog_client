package com.example.skylog.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import com.example.skylog.R
import com.example.skylog.data.BasicInfo
import com.example.skylog.data.Basic_Info
import com.example.skylog.data.Bbox_line
import com.example.skylog.data.My_MQTT_info
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import android.util.Base64
import android.util.Log
import com.example.skylog.MQTT_helper.MqttHelper
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage



class system_info : Fragment(), CoroutineScope {

    public val fragment_name:String="system_info"
    var mqttHelper:MqttHelper?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        var rootView:View=inflater.inflate(R.layout.fragment1_sys_info, container, false)
        var table: TableLayout = rootView.findViewById(R.id.CraneTable)
        table.setStretchAllColumns(true)
        table.setShrinkAllColumns(true)
        return rootView
    }


    //-------Работа с сетью---------------------------------------------

    private val rootJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob


   private fun ReadInfoWork() = launch {
       Mqtt_load_basic_info()
        while(true) {
            if (mqttHelper!!.check_connection()) {
                // запуск режима онлайн с периодичностью в 1с
                val cmd: String = My_MQTT_info.ChoosenBlock + My_MQTT_info.cmd.cmd_bbox
                val topic: String = My_MQTT_info.ChoosenBlock + My_MQTT_info.info.info_basic__online
                mqttHelper!!.publishMessage("section=basic_info",0,cmd)  //посылаем неизвестyю команду для получения ответа
                mqttHelper!!.subscribeToTopic(topic)
                break;
                //database
            }

            delay(1000)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //you can set the title for your toolbar here for different fragments different titles
       // activity!!.title = "Menu 1"
    }
//-----------------------------------------------------------------------------------
    private fun Mqtt_load_basic_info() {
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

    private fun parse_data(mqttMessage:MqttMessage) {
        val string = mqttMessage.payload.toString()
        val encodeValue = Base64.decode(mqttMessage.payload, Base64.DEFAULT)
        BasicInfo.device_name=encodeValue[0].toString()
        BasicInfo.device_number=encodeValue[0].toString()
        BasicInfo.pribor_number=encodeValue[0].toString()
        BasicInfo.install_date.Y        = encodeValue[0].toInt()
        BasicInfo.install_date.Mon      = encodeValue[0].toInt()
        BasicInfo.install_date.D        = encodeValue[0].toInt()
        BasicInfo.install_date.H        = encodeValue[0].toInt()
        BasicInfo.install_date.M        = encodeValue[0].toInt()
        BasicInfo.install_date.S        = encodeValue[0].toInt()

        BasicInfo.setup_date.Y          = encodeValue[0].toInt()
        BasicInfo.setup_date.Mon        = encodeValue[0].toInt()
        BasicInfo.setup_date.D          = encodeValue[0].toInt()
        BasicInfo.setup_date.H          = encodeValue[0].toInt()
        BasicInfo.setup_date.M          = encodeValue[0].toInt()
        BasicInfo.setup_date.S          = encodeValue[0].toInt()

        BasicInfo.read_date.Y           = encodeValue[0].toInt()
        BasicInfo.read_date.Mon         = encodeValue[0].toInt()
        BasicInfo.read_date.D           = encodeValue[0].toInt()
        BasicInfo.read_date.H           = encodeValue[0].toInt()
        BasicInfo.read_date.M           = encodeValue[0].toInt()
        BasicInfo.read_date.S           = encodeValue[0].toInt()

    }
}