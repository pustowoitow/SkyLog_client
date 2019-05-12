package com.example.skylog.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import com.example.skylog.R


class system_info : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        var rootView:View=inflater.inflate(R.layout.fragment1_sys_info, container, false)
        var table: TableLayout = rootView.findViewById(R.id.CraneTable)
        table.setStretchAllColumns(true)
        table.setShrinkAllColumns(true)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //you can set the title for your toolbar here for different fragments different titles
       // activity!!.title = "Menu 1"
    }
}