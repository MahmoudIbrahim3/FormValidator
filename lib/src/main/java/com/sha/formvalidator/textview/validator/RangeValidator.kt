package com.sha.formvalidator.textview.validator

import android.widget.TextView

/**
 * A validator that returns true only if the input field contains only numbers
 * and the number is within the given range.
 *
 */
class RangeValidator(errorMessage: String, private val min: Int, private val max: Int) : Validator(errorMessage) {

    override fun isValid(tv: TextView): Boolean {
        val length = tv.text.toString().length
        return length in min..max
    }
}
