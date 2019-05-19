package com.example.skylog.Fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TextView
import com.example.skylog.MQTT_helper.MqttHelper
import com.example.skylog.R
import com.example.skylog.data.MQTT_info
import com.example.skylog.data.My_MQTT_info
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage

class send_msg : Fragment() {

    public val fragment_name:String="send_msg"
    var mqttHelper:MqttHelper?=null
    var RootView: View? = null
    var my_context: Context?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        RootView=inflater.inflate(R.layout.fragment5_send_msg, container, false)
        my_context=RootView!!.context

        var button:Button=RootView!!.findViewById(R.id.send)
        var message: EditText =RootView!!.findViewById(R.id.message)
        Mqtt_start()
        button.setOnClickListener {
            var topic=My_MQTT_info.ChoosenBlock+My_MQTT_info.cmd.cmd_message
            var msg:String=message.text.toString()
            mqttHelper!!.publishMessage(msg, 0, topic)
        }

        return RootView
    }

    private fun Mqtt_start() {
        mqttHelper = MqttHelper(context!!)//(getActivity()!!.getApplicationContext())
        mqttHelper!!.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                My_MQTT_info.Connection_status=true
            }

            override fun connectionLost(throwable: Throwable) {
                My_MQTT_info.Connection_status=false
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                Log.w("Debug", mqttMessage.toString())
            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
                Log.w("Debug", iMqttDeliveryToken.toString())
                Snackbar.make(RootView!!, "Arrived", Snackbar.LENGTH_INDEFINITE).show()


            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //you can set the title for your toolbar here for different fragments different titles
        // activity!!.title = "Menu 1"
    }
}