package com.app.rewizor.ui

import android.app.Dialog
import android.os.Bundle
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class PickerDialog : DialogFragment() {
    lateinit var listener: NumberListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null) return super.onCreateDialog(savedInstanceState)

        val picker = NumberPicker(activity)
            .apply {
                minValue = 3
                maxValue = 60
            }
        picker.setOnValueChangedListener { _, _, newVal ->
            listener.onChanged(newVal)
        }
        return AlertDialog.Builder(activity!!).run {
            setTitle("Выберите возраст")
            setNegativeButton("Отмена") { _, _ -> listener.onCleared() }
            setView(picker)
            create()
        }
    }

    fun showDialog(fm: FragmentManager) {
        show(fm, "PickerDialog")
    }

    interface NumberListener {
        fun onChanged(value: Int)
        fun onCleared()
    }
}