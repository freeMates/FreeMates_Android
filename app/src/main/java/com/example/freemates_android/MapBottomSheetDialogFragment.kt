package com.example.freemates_android

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.freemates_android.sheet.CategoryResultSheet
import com.example.freemates_android.sheet.PlacePreviewSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MapBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private var currentTag: String? = null
    private lateinit var behavior: BottomSheetBehavior<FrameLayout> // ★ 추가
    private var readyAction: ((MapBottomSheetDialogFragment) -> Unit)? = null
    private var peekHeight: Int = 0

    private fun currentInnerFragment(): Fragment? =
        childFragmentManager.findFragmentById(R.id.sheet_container)

    /** 호출 시점 보장용 */
    fun doWhenSheetReady(action: (MapBottomSheetDialogFragment) -> Unit) {
        readyAction = action
        if (dialog?.isShowing == true && isAdded) action(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).apply {

            this@MapBottomSheetDialogFragment.behavior = behavior.apply {
                isFitToContents = false
                peekHeight = resources.getDimensionPixelSize(R.dimen.peek_height)
                isHideable = false
                skipCollapsed = false
                halfExpandedRatio = 0.5f
                expandedOffset = 100
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

            peekHeight = behavior.peekHeight
            Log.d("peekHeight : ", peekHeight.toString())

            window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

            setOnShowListener {
                val sheet = findViewById<FrameLayout>(
                    com.google.android.material.R.id.design_bottom_sheet
                ) ?: return@setOnShowListener

                // BottomNavigationView 높이만큼 margin
                val navHeight = requireActivity()
                    .findViewById<View>(R.id.bottom_navigation).height

                val insetBottom = WindowInsetsCompat.toWindowInsetsCompat(
                    sheet.rootWindowInsets
                ).getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

                Log.d("태그 제발 : ", "")
                val safeBottom = navHeight + insetBottom

                (sheet.layoutParams as CoordinatorLayout.LayoutParams).bottomMargin = safeBottom
                sheet.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.roundedbox_top_20)

                sheet.setPadding(0, 0, 0, insetBottom)

//                behavior.peekHeight      += safeBottom
                behavior.expandedOffset   = safeBottom

                readyAction?.invoke(this@MapBottomSheetDialogFragment)
                readyAction = null
            }
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
    ): View = inflater.inflate(
        R.layout.fragment_map_bottom_sheet_dialog, container, false
    )

    fun collapse() =
        if (::behavior.isInitialized) {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else Unit

    fun updateContent(state: MapViewModel.SheetState) {
        val (fragment, tag) = when (state) {
            is MapViewModel.SheetState.PlacePreview ->
                PlacePreviewSheet.newInstance(state.place) to "PlacePreview"
            is MapViewModel.SheetState.CategoryResult ->
                CategoryResultSheet.newInstance(state.category, state.places) to "CategoryResult"
            else -> return
        }
        if (tag == currentTag) return  // 이미 같은 화면

        childFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.sheet_container, fragment, tag)
            .commit()

        currentTag = tag
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}