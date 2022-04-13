package com.eg.app.util

import android.app.Activity
import android.view.Gravity
import androidx.appcompat.app.AlertDialog
import android.widget.ArrayAdapter
import com.eg.R


class AlertDialogUtils {

    companion object {

        fun show(activity: Activity, title: String = "", message: String = "", positive: () -> Unit, negative: () -> Unit) {
            val dialog = AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(R.string.ensure) { _, _ -> positive.invoke() }
                    .setNegativeButton(R.string.cancel) { _, _ -> negative.invoke() }
                    .create()
            dialog.setCancelable(false)
            dialog.show()
            setCenter(dialog)
            //修改颜色
//            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
//            try {
//                val mAlert = AlertDialog::class.java.getDeclaredField("mAlert")
//                mAlert.isAccessible = true
//                val mAlertController = mAlert.get(dialog)
//                val mMessage = mAlertController.javaClass.getDeclaredField("mMessageView")
//                mMessage.isAccessible = true
//                val textView = mMessage.get(mAlertController) as TextView
//                textView.setTextColor(Color.BLUE)
//            } catch (e: Exception) {
//
//            }
        }

        fun show(activity: Activity, title: String = "", message: String = "", neutral: () -> Unit, positive: () -> Unit, negative: () -> Unit) {
            val dialog = AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(R.string.ensure) { _, _ -> positive.invoke() }
                    .setNegativeButton(R.string.cancel) { _, _ -> negative.invoke() }
                    .setNeutralButton("其他") { _, _ -> neutral.invoke() }
                    .create()
            dialog.setCancelable(false)
            dialog.show()
            setCenter(dialog)
        }

//        fun showImg(activity: Activity, title: String, qrCodeString: String) {
//            val view: View = LayoutInflater.from(activity).inflate(R.layout.layout_custom_dialog, null)
//            val dialog = AlertDialog.Builder(activity)
//                    .setView(view)
//                    .create()
//            dialog.setCancelable(false)
//            dialog.show()
//            view.findViewById<Button>(R.id.negativeButton).setOnClickListener {
//                dialog.dismiss()
//            }
//            val qrCode = view.findViewById<ImageView>(R.id.qrCode)
//            val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
//            tvTitle.text = title
//            val bitmap = CodeCreator.createQRCode(qrCodeString, 500, 500, null)
//            qrCode.setImageBitmap(bitmap)
//            setCenter2(dialog)
//        }

        fun show(activity: Activity, title: String = "", message: String = "", positive: () -> Unit) {
            val dialog = AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(R.string.ensure) { _, _ -> positive.invoke() }
                    .create()
            dialog.setCancelable(false)
            dialog.show()
            setCenter(dialog)
        }

        /**
         * 条目
         */
        fun showItems(activity: Activity, title: String = "", item: Array<String>, positive: (position: Int) -> Unit) {
            val dialog = AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setItems(item) { _, p1: Int -> positive.invoke(p1) }
                    .create()
//            dialog.setCancelable(false)
            dialog.show()
            setCenter(dialog)
        }

        /**
         * 单选
         */
        fun showSingleChoiceItems(activity: Activity, title: String = "", item: Array<String>, positive: (position: Int) -> Unit) {
            val dialog = AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setSingleChoiceItems(item, 1) { _, p1 -> positive.invoke(p1) }
                    .create()
//            dialog.setCancelable(false)
            dialog.show()
            setCenter(dialog)
        }

        fun showSingleItems(activity: Activity, title: String = "", item: Array<String>, positive: (position: Int) -> Unit) {
            val dialog = AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setItems(item) { _, p1 -> positive.invoke(p1) }
                    .create()
//            dialog.setCancelable(false)
            dialog.show()
            setCenter(dialog)
        }

        fun showSingleChoiceItems2(activity: Activity, title: String = "", item: Array<String>, checkItemIndes: Int = 0, positive: (position: Int) -> Unit) {
            var select = checkItemIndes
            val adapter: ArrayAdapter<CharSequence> = ArrayAdapter<CharSequence>(activity, R.layout.my_select_dialog_singlechoice_material, item)
            val dialog = AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setSingleChoiceItems(adapter, checkItemIndes) { _, p1 -> select = p1 }
                    .setPositiveButton(R.string.ensure) { _, _ -> positive.invoke(select) }
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .create()
            dialog.setCancelable(false)
            dialog.show()
            setCenter(dialog)
        }

        fun showSingleChoiceItems3(activity: Activity, title: String = "", item: Array<String>, checkItemIndes: Int = 0, neutral: (position: Int) -> Unit, positive: (position: Int) -> Unit) {
            var select = checkItemIndes
            val dialog = AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setSingleChoiceItems(item, checkItemIndes) { _, p1 -> select = p1 }
                    .setNeutralButton("删除") { _, _ -> neutral.invoke(select) }
                    .setPositiveButton(R.string.ensure) { _, _ -> positive.invoke(select) }
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .create()
            dialog.setCancelable(false)
            dialog.show()
            setCenter(dialog)
        }

        /**
         * 多选
         */
        fun showMultiChoiceItems(activity: Activity, title: String = "", item: Array<String>, item1: BooleanArray, positive: (item: BooleanArray) -> Unit, neutral: () -> Unit) {
            val dialog = AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMultiChoiceItems(item, item1) { p0, p1, p2 -> item1[p1] = p2 }
                    .setPositiveButton(R.string.ensure) { _, _ -> positive.invoke(item1) }
                    .setNegativeButton(R.string.cancel) { _, _ -> neutral.invoke() }
                    .create()
            dialog.setCancelable(false)
            dialog.show()
            setCenter(dialog)
        }

        /**
         * 解决部分机型 不居中bug
         */
        private fun setCenter(dialog: AlertDialog) {
            dialog.window?.run {
                attributes = attributes.apply {
                    width = (windowManager.defaultDisplay.width * 0.95).toInt()
                    gravity = Gravity.CENTER
                }
            }
        }


        private fun setCenter2(dialog: AlertDialog) {
            dialog.window?.run {
                attributes = attributes.apply {
                    width = (windowManager.defaultDisplay.width * 0.8).toInt()
                    gravity = Gravity.CENTER
                }
            }
        }
    }
}