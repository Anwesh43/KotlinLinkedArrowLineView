package com.anwesh.uiprojects.kotlinlinkedarrowlineview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.linkedarrowlineview.LinkedArrowLineView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LinkedArrowLineView.create(this)
    }
}
