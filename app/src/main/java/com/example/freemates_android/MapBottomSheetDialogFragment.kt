package com.example.freemates_android

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import com.example.freemates_android.sheet.CategoryResultSheet
import com.example.freemates_android.sheet.FavoriteDetailSheet
import com.example.freemates_android.sheet.FavoriteListSheet
import com.example.freemates_android.sheet.PlacePreviewSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MapBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private var currentFragmentTag: String? = null
    private lateinit var behavior: BottomSheetBehavior<FrameLayout> // ★ 추가
    private var readyAction: ((MapBottomSheetDialogFragment) -> Unit)? = null

    /** 호출 시점 보장용 */
    fun doWhenSheetReady(action: (MapBottomSheetDialogFragment) -> Unit) {
        readyAction = action
        // 이미 보여진 뒤라면 즉시 실행
        if (dialog?.isShowing == true) action(this)
        if (isAdded) action(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.apply {
            behavior = this
            isFitToContents = false
            peekHeight = resources.getDimensionPixelSize(R.dimen.peek_height)  // ★ 추가
            isHideable = false                                                 // ★ 추가
            skipCollapsed = false
            halfExpandedRatio = 0.5f
            expandedOffset = 100
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        dialog.setOnShowListener {
            val bottomSheet =
                dialog.findViewById<FrameLayout>(
                    com.google.android.material.R.id.design_bottom_sheet)

            // ① BottomNavigationView 높이를 동적으로 구한다
            val navHeight = requireActivity()
                .findViewById<View>(R.id.bottom_navigation).height

            // ② design_bottom_sheet 에 마진 or 패딩을 준다
            bottomSheet?.let { sheet ->
                // 마진 방식
                (sheet.layoutParams as CoordinatorLayout.LayoutParams).apply {
                    bottomMargin = navHeight
                }
                sheet.requestLayout()

                // 배경도 같이 둥글게
                sheet.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.roundedbox_top_20)

                val insetBottom = WindowInsetsCompat.toWindowInsetsCompat(
                    sheet.rootWindowInsets
                ).getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                sheet.setPadding(0, 0, 0, navHeight + insetBottom)
            }

            readyAction?.invoke(this@MapBottomSheetDialogFragment)  // ★ 준비 완료 알림
            readyAction = null
        }

        return dialog
    }

    private fun adjustForBottomNav(bottomSheetDialog: BottomSheetDialog) {
        val bottomNav = requireActivity().findViewById<View>(R.id.bottom_navigation)

        bottomNav.post {                       // ★ 높이 계산이 끝난 뒤 실행
            val navHeight = bottomNav.height
            if (navHeight == 0) return@post    // 아직 계산 안 됐으면 패스

            // ① 시트를 nav 높이만큼 위로
            val sheet = bottomSheetDialog.findViewById<FrameLayout>(
                com.google.android.material.R.id.design_bottom_sheet
            ) ?: return@post

            sheet.layoutParams =
                (sheet.layoutParams as CoordinatorLayout.LayoutParams).apply {
                    bottomMargin = navHeight
                }

            // ② 네비게이션 영역이 투명하게 보이도록 배경+insets 처리
            sheet.setBackgroundResource(R.drawable.roundedbox_top_20)
            val insetBottom = WindowInsetsCompat
                .toWindowInsetsCompat(sheet.rootWindowInsets)
                .getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            sheet.setPadding(0, 0, 0, navHeight + insetBottom)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_map_bottom_sheet_dialog, container, false)
    }

    fun collapse() {                                                   // ★ 추가
        if (::behavior.isInitialized) {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    fun updateContent(state: MapViewModel.SheetState) {
        if (state is MapViewModel.SheetState.Collapsed) {              // ★ 추가
            collapse()
            return
        }

        val (fragment, tag) = when (state) {
            is MapViewModel.SheetState.PlacePreview ->
                PlacePreviewSheet.newInstance(state.place) to "PlacePreview"
            is MapViewModel.SheetState.CategoryResult ->
                CategoryResultSheet.newInstance(state.category, state.places) to "CategoryResult"
//            is MapViewModel.SheetState.FavoriteList ->
//                FavoriteListSheet.newInstance(state.lists) to "FavoriteList"
//            is MapViewModel.SheetState.FavoriteDetail ->
//                FavoriteDetailSheet.newInstance(state.list) to "FavoriteDetail"
            else -> return
        }

        if (tag == currentFragmentTag) return

        childFragmentManager.beginTransaction()
            .replace(R.id.sheet_container, fragment, tag)
            .commit()

        currentFragmentTag = tag
    }
}