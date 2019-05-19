package com.example.skylog.data

data class MQTT_info (var serverUri:String, var login:String, var password:String){

    public var token:String=""
    public var Connection_status:Boolean=false
    public var Subscribe_status:Boolean=false
    public var Publish_status:Boolean=false
    public var ChoosenBlock:String=""


public val BlocksNames:List<String> =listOf<String>(
        //"1028139000003",
        "0",
        "1028139000010",
        "1028139000005",
        "1028139000015"
    )
public val cmd:commands= commands()
public val acks:acknolegments=acknolegments()
public val info:Info=Info()
}

class commands
{
    val cmd_unknown:String="/cmd/fghnf"     //Неизвестная команда
    val cmd_online:String="/cmd/online"     //Включение режима онлайн для блока
    val cmd_message:String="/cmd/message"     //Включение режима онлайн для блока
    val cmd_bbox:String="/cmd/bbox"         //команда считывания черного ящика (передается данные кол-ва строк, которые нужно получить)
    val cmd_bbox_online:String="/cmd/bbox_online"   //передача всех новых строк при включении режима
    val cmd_upload:String="/cmd/upload"             // запрос секций для чтения
}

class acknolegments
{
    val ping:String="/evt/ping"
    val cmdack_cmds:String="/evt/cmds"
    val cmdack_online:String="/cmdack/online"
    val cmdack_bbox:String="/cmdack/bbox"       //все данные прочитаны
    val cmdack_upload_info:String="/evt/upload_info"   //передается название пакета и его размер
}

class Info
{
    val info_basic__online:String="/evt/bbox/00207/basic_info"
    val info_tpchr_online:String="/evt/online/fmt/bin/00207"
    val info_bbox:String="/evt/bbox/json"
}

var My_MQTT_info:MQTT_info=MQTT_info("tcp://mx3.rez.ru:18833","1028139000099", "xvRN3AfUo")
