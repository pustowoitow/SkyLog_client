package com.example.skylog.MQTT_helper

import android.content.Context
import android.util.Log
import com.example.skylog.data.My_MQTT_info
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.android.service.MqttAndroidClient
import android.support.annotation.NonNull
import java.io.UnsupportedEncodingException


class MqttHelper(context: Context) {
    var mqttAndroidClient: MqttAndroidClient

    internal val serverUri = My_MQTT_info.serverUri      //"tcp://mx3.rez.ru:18833"  //"tcp://m12.cloudmqtt.com:11111"

    internal val clientId:String = My_MQTT_info.login    //"ExampleAndroidClient"  "1026918000013"//
    internal val subscriptionTopic = My_MQTT_info.BlocksNames[0]+ My_MQTT_info.acks.cmdack_cmds       //"1028139000010/+"

    internal val username = My_MQTT_info.login               //  "1026918000013"//
    internal val password = My_MQTT_info.password          //"xvRN3AfUo"

    public var ConectionStatus:Int=0


    init {
        mqttAndroidClient = MqttAndroidClient(context, serverUri, clientId)
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                Log.w("mqtt", s)
            }

            override fun connectionLost(throwable: Throwable) {

            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                Log.w("Mqtt", mqttMessage.toString())
            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {

            }
        })
        connect()
    }

    fun setCallback(callback: MqttCallbackExtended) {
        mqttAndroidClient.setCallback(callback)
    }


    fun check_connection():Boolean {
        return mqttAndroidClient.isConnected
    }



    private fun connect() {
        val mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.isAutomaticReconnect = true
        mqttConnectOptions.isCleanSession = false
        mqttConnectOptions.userName = username
        mqttConnectOptions.password = password.toCharArray()

        try {

            mqttAndroidClient.connect(mqttConnectOptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {

                    val disconnectedBufferOptions = DisconnectedBufferOptions()
                    disconnectedBufferOptions.isBufferEnabled = true
                    disconnectedBufferOptions.bufferSize = 100
                    disconnectedBufferOptions.isPersistBuffer = false
                    disconnectedBufferOptions.isDeleteOldestMessages = false
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions)
                    Log.w("Mqtt", "Connected to: $serverUri")
                    ConectionStatus=1
                    /*subscribeToTopic(subscriptionTopic)
                    publishMessage("", 0, My_MQTT_info.BlocksNames[0]+My_MQTT_info.cmd.cmd_unknown)*/
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.w("Mqtt", "Failed to connect to: $serverUri$exception")
                }
            })


        } catch (ex: MqttException) {
            ex.printStackTrace()
        }

    }

    fun subscribeToTopic(subscriptionTopic:String) {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, object : IMqttActionListener {
                override fun onSuccess( asyncActionToken: IMqttToken) {
                    Log.w("Mqtt","Subscribed to $subscriptionTopic");
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.w("Mqtt", "Subscribed fail!")
                }
            })

        } catch (ex: MqttException) {
            System.err.println("Exceptionst subscribing")
            ex.printStackTrace()
        }
    }

    @Throws(MqttException::class, UnsupportedEncodingException::class)
    fun publishMessage(
        msg: String, qos: Int, topic: String
    ) {
        var encodedPayload = ByteArray(0)
        encodedPayload = msg.toByteArray(charset("UTF-8"))
        val message = MqttMessage(encodedPayload)
        message.id = 5866
        message.isRetained = true
        message.qos = qos
        mqttAndroidClient.publish(topic, message, null, object : IMqttActionListener {
            override fun onSuccess( asyncActionToken: IMqttToken) {
                Log.w("Mqtt","Published to $topic");
            }

            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                Log.w("Mqtt", "Publish fail!")
            }
        })
    }

}