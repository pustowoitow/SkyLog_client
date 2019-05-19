package com.example.skylog.data

var BasicInfo:Basic_Info=Basic_Info()

class Basic_Info {
    public var device_name          :String
    public var device_number        :String
    public var pribor_number        :String
    public var install_date     :TDate
    public var setup_date       :TDate
    public var read_date        :TDate

    init {
        device_name  ="---"
        device_number="---"
        pribor_number="---"
        install_date = TDate()
        setup_date  =TDate()
        read_date=TDate()
    }
}

class TDate
{
    public var S        :Int	// Секунды
    public var M	     :Int// Минуты
    public var H         :Int// Часы
    public var Y	     :Int// Год
    public var Mon	     :Int// Месяц
    public var D	     :Int// День
    init {
        S=0
        M=0
        H=0
        Y=0
        Mon=0
        D=0
    }
}