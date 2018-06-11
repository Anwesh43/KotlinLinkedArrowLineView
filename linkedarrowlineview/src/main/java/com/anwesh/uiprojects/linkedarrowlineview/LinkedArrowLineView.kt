package com.anwesh.uiprojects.linkedarrowlineview

/**
 * Created by anweshmishra on 11/06/18.
 */

import android.content.*
import android.graphics.*
import android.content.Context
import android.view.View
import android.view.MotionEvent

val ARROW_LINE_NODES = 5

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

    data class State(var j : Int = 0, var prevScale : Float = 0f, var dir : Float = 0f) {

        val scales : Array<Float> = arrayOf(0f, 0f)

        fun update(stopcb : (Float) -> Unit) {
            scales[j] += 0.1f * dir
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += dir.toInt()
                if (j == scales.size || j == -1) {
                    j -= dir.toInt()
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {
        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch (ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = false
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class ArrowLineNode(var i : Int, val state : State = State()) {

        var next : ArrowLineNode? = null

        var prev : ArrowLineNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < ARROW_LINE_NODES - 1) {
                next = ArrowLineNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val GAP : Float = w / ARROW_LINE_NODES
            paint.strokeWidth = GAP/12
            paint.color = Color.WHITE
            paint.strokeCap = Paint.Cap.ROUND
            prev?.draw(canvas, paint)
            canvas.save()
            canvas.translate(i * GAP, h/2)
            val path = Path()
            path.addRect(RectF(0f, -GAP/2, GAP, GAP/2), Path.Direction.CW)
            canvas.clipPath(path)
            canvas.save()
            canvas.translate(GAP * state.scales[0], 0f)
            for (i in 0..1) {
                canvas.save()
                canvas.rotate(45f * (1 - 2 * i))
                canvas.drawLine(-GAP / 5, 0f, 0f, 0f, paint)
                canvas.restore()
            }
            canvas.restore()
            canvas.restore()
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }

        fun getNeighbor(dir : Int, cb : () -> Unit) : ArrowLineNode {
            var curr : ArrowLineNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class LinkedArrowLine (var i : Int) {
        var dir : Int = 1

        var curr : ArrowLineNode = ArrowLineNode(0)

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(stopcb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNeighbor(dir) {
                    dir *= -1
                }
                stopcb(it)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            curr.startUpdating(startcb)
        }
    }
}