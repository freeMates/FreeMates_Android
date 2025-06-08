package com.example.freemates_android

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.graphics.drawable.toDrawable


class LoadingDialog(private val context: Context) {

    private var dialog: AlertDialog? = null

    fun showLoading() {
        if (dialog?.isShowing == true) return // 이미 표시 중이면 무시

        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.dialog_loading, null)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        val builder = AlertDialog.Builder(context)
            .setCancelable(false)
            .setView(view)
        dialog = builder.create()

        // 배경을 투명하게 설정
        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        dialog?.show()
    }

    fun hideLoading() {
        dialog?.dismiss()
    }
}