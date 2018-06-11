package com.anwesh.uiprojects.linkedarrowlineview

/**
 * Created by anweshmishra on 11/06/18.
 */

import android.content.*
import android.graphics.*
import android.content.Context
import android.view.View
import android.view.MotionEvent

class LinkedArrowLineView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}