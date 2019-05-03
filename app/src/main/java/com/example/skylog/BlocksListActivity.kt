package com.example.skylog

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.Log
import com.example.skylog.MQTT_helper.MqttHelper
import com.example.skylog.data.My_MQTT_info
import kotlinx.android.synthetic.main.activity_blocks__list.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*
import okhttp3.Request
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlin.coroutines.CoroutineContext
import android.R.attr.button
import android.graphics.drawable.Drawable
import android.R.attr.button
import android.graphics.drawable.ColorDrawable





class BlocksListActivity : AppCompatActivity(), CoroutineScope {

    var mqttHelper:MqttHelper?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocks__list)
        //
        My_MQTT_info.Connection_status=false
        Mqtt_check_blocks()

        block1.setOnClickListener {
            My_MQTT_info.ChoosenBlock="1026918000013"
            val intent = Intent(this, MainActivity::class.java)
            if ((block1.getBackground() as ColorDrawable).color==Color.GREEN)startActivity(intent)
        }
        block2.setOnClickListener {
            My_MQTT_info.ChoosenBlock="1028139000010"
            val intent = Intent(this, MainActivity::class.java)
            if ((block2.getBackground() as ColorDrawable).color==Color.GREEN)startActivity(intent)
        }
        block3.setOnClickListener {
            My_MQTT_info.ChoosenBlock="1028139000005"
            val intent = Intent(this, MainActivity::class.java)
            if ((block3.getBackground() as ColorDrawable).color==Color.GREEN) startActivity(intent)
        }
        block4.setOnClickListener {
            My_MQTT_info.ChoosenBlock="1028139000015"
            val intent = Intent(this, MainActivity::class.java)
            if ((block4.getBackground() as ColorDrawable).color==Color.GREEN)startActivity(intent)
        }
        block5.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            if ((block5.getBackground() as ColorDrawable).color==Color.GREEN) startActivity(intent)
        }
        PublishData()
    }

    private fun Mqtt_check_blocks() {
        // Snackbar.make(email, "Hello", Snackbar.LENGTH_INDEFINITE).show()
        mqttHelper = MqttHelper(applicationContext)
        mqttHelper!!.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                Snackbar.make(email, "Complete", Snackbar.LENGTH_INDEFINITE).show()
                My_MQTT_info.Connection_status=true
            }

            override fun connectionLost(throwable: Throwable) {
                Snackbar.make(email, "Lost", Snackbar.LENGTH_INDEFINITE).show()
                My_MQTT_info.Connection_status=false
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                Log.w("Debug", mqttMessage.toString())
                val color =  Color.GREEN
                if (topic.equals(My_MQTT_info.BlocksNames[0] + My_MQTT_info.acks.cmdack_cmds)) block1.setBackgroundColor(color)
                else if (topic.equals(My_MQTT_info.BlocksNames[1] + My_MQTT_info.acks.cmdack_cmds)) block2.setBackgroundColor(color)
                else if (topic.equals(My_MQTT_info.BlocksNames[2] + My_MQTT_info.acks.cmdack_cmds)) block3.setBackgroundColor(color)
                else if (topic.equals(My_MQTT_info.BlocksNames[3] + My_MQTT_info.acks.cmdack_cmds)) block4.setBackgroundColor(color)
            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
                Log.w("Debug", iMqttDeliveryToken.toString())
              //  Snackbar.make(email, iMqttDeliveryToken.toString(), Snackbar.LENGTH_INDEFINITE).show()
             //   My_MQTT_info.token=iMqttDeliveryToken.toString()

            }
        })

        val topic:String=My_MQTT_info.BlocksNames[0] + My_MQTT_info.acks.cmdack_cmds
        //mqttHelper.subscribeToTopic(topic)

        val topic2:String=My_MQTT_info.BlocksNames[0] + "/cmd/dfjid"
       // mqttHelper.publishMessage("", 0, topic2)  //посылаем неизвестyю команду для получения ответа


    }

//-------Работа с сетью в фоне---------------------------------------------
    private val rootJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

    var flag_subscribed:Boolean=false
    private fun PublishData() = launch {
        while(true) {
           // if (mqttHelper!!.ConectionStatus == 1) {
                if (mqttHelper!!.check_connection())
                {
                    for (i:String in My_MQTT_info.BlocksNames) {
                        if (!flag_subscribed)mqttHelper!!.subscribeToTopic(i + My_MQTT_info.acks.cmdack_cmds)
                        mqttHelper!!.publishMessage("1", 0, i + "/cmd/dfjid")
                    }
                    flag_subscribed=true
                }
           // }
            delay(5000)
        }

    }


    override fun onDestroy() {
        rootJob.cancel()
        super.onDestroy()
    }

}
