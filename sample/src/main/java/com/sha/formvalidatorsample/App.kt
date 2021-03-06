package com.sha.formvalidatorsample

import android.app.Application
import com.sha.formvalidator.textview.TextViewValidators
import com.sha.formvalidatorsample.validator.NumberOneCustomValidator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        TextViewValidators.customValidators = listOf(NumberOneCustomValidator("Value doesn't equal 1"))
    }
}
