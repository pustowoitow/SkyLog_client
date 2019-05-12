package com.example.skylog.data

data class Bbox_line(var LineNumber:Int) {
        public var wind			    :Int
        public var outs6_13			:Int
        public var operatorintervent	:Boolean
        public var instal       	    :Boolean

        public var ugaz		        :Int
        public var sugaz		        :Boolean
        public var linkont			    :Boolean
        public var kratzapas		    :Boolean
        public var zeroput			    :Boolean

        public var s		        :Int
        public var q		        :Int
        public var r		        :Int
        public var h               :Int
        public var qm              :Int
        public var m               :Int
        public var outs1_5         :Int

        public var dpm_s  	        :Int
        public var dpm_az          :Int
        public var dpm_r           :Int
        public var dpm_h           :Int
        public var force_dat       :Int
    init {
        wind			    =0
        outs6_13			=0
        operatorintervent	=false
        instal       	    =false

        ugaz		        =0
        sugaz		        =false
        linkont			    =false
        kratzapas		    =false
        zeroput			    =false

        s		        =0
        q		        =0
        r		        =0
        h               =0
        qm              =0
        m               =0
        outs1_5         =0

        dpm_s  	        =0
        dpm_az          =0
        dpm_r           =0
        dpm_h           =0
        force_dat       =0
    }
}