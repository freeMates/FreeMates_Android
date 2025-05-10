package com.example.freemates_android

import android.content.Context
import android.view.WindowManager
import android.view.LayoutInflater
import android.widget.ImageView
import android.graphics.PixelFormat
import android.view.Gravity

object HeartOverlayUtil {
    fun showHeartOverlay(context: Context) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val inflater = LayoutInflater.from(context)

        // 오버레이 레이아웃 생성
        val heartView = inflater.inflate(R.layout.overlay_heart, null)
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION,  // 오버레이 대신 일반 타입 사용
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }

        windowManager.addView(heartView, layoutParams)

        // 애니메이션 실행
        val heartImageView = heartView.findViewById<ImageView>(R.id.ivHeartOverlay)
        heartImageView.scaleX = 0.2f
        heartImageView.scaleY = 0.2f
        heartImageView.alpha = 0f

        heartImageView.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(750) // 빠르게 나타나기
            .withEndAction {
                heartImageView.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(0f)
                    .setDuration(500) // 빠르게 사라지기
                    .withEndAction {
                        windowManager.removeView(heartView)
                    }
            }.start()
    }
}