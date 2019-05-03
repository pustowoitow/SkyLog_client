package com.example.skylog.data

data class Bbox_table(val params:String){
    init {
        var ugstr			    :Int
        var instal			    :Int

        var uggus			    :Int
        var operatorintervent	:Int

        var lstr		        :Int
        var head		        :Int

        var lgus		        :Int
        var pol			        :Int
        var out1		        :Int
        var out2		        :Int
        var out3		        :Int

        var m			        :Int
        var m_inactive	        :Int

        var force               :Int
        var q                   :Int
        var q_inactive          :Int
        var r                   :Int
        var h                   :Int
        var qm                  :Int

        var ugaz			:Int
        var index_go			:Boolean
        var index_counterweight	:Boolean
        var index_angle_twr		:Boolean

        var blk_hard_error		:Boolean
        var blk_feat			:Boolean
        var blk_two_hoist		:Boolean
        var blk_moment		    :Boolean
        var blk_radius_min		:Boolean
        var blk_radius_max		:Boolean
        var blk_hook_up		    :Boolean
        var blk_qm			    :Boolean

        var blk_cutoff_radius_min	:Boolean
        var blk_cutoff_radius_max	:Boolean
        var blk_cutoff_ug_str_min	:Boolean
        var blk_cutoff_ug_str_max	:Boolean
        var index_lift		        :Boolean
        var index_boom_type	        :Boolean
        var cycle			        :Boolean
    }
}