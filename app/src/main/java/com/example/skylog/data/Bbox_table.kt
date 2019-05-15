package com.example.skylog.data

data class Bbox_line(var Number:Int) {
    public var ID			    :Int
    public var year			    :Int
    public var month			    :Int
    public var day			    :Int
    public var hour			    :Int
    public var minute			    :Int
    public var seconds			    :Int

        public var wind			    :Int
        public var outs6_13			:Int
        public var operatorintervent	:Boolean
        public var instal       	    :Boolean

        public var ugaz		        :Float
        public var sugaz		        :Boolean
        public var linkont			    :Boolean
        public var kratzapas		    :Boolean
        public var zeroput			    :Boolean

        public var s		        :Float
        public var q		        :Float
        public var r		        :Float
        public var h               :Float
        public var qm              :Float
        public var m               :Int
        public var outs1_5         :Int

        public var dpm_s  	        :Int
        public var dpm_az          :Int
        public var dpm_r           :Int
        public var dpm_h           :Int
        public var force_dat       :Int
    init {
        ID=0
        year=2000
        month=1
        day=1
        hour=0
        minute=0
        seconds=0

        wind			    =0
        outs6_13			=0
        operatorintervent	=false
        instal       	    =false

        ugaz		        = 0F
        sugaz		        =false
        linkont			    =false
        kratzapas		    =false
        zeroput			    =false

        s		        =0F
        q		        =0F
        r		        =0F
        h               =0F
        qm              =0F
        m               =0
        outs1_5         =0

        dpm_s  	        =0
        dpm_az          =0
        dpm_r           =0
        dpm_h           =0
        force_dat       =0
    }
}