package com.sha.formvalidator.textview.validator.composite

import android.widget.TextView
import com.sha.formvalidator.textview.validator.Validator

/**
 * The AND validator checks if all of the passed validators is returning true.<br></br>
 * Note: the message that will be shown is the one of the first failing validator
 *
 */
class AndValidator : CompositeValidator {

    constructor(vararg validators: Validator) : super("", *validators)

    constructor() : super("")

    override fun isValid(tv: TextView): Boolean {
        for (v in validators) {
            if (!v.isValid(tv)) {
                this.errorMessage = v.errorMessage
                return false // Remember :) We're acting like an || operator.
            }
        }
        return true
    }
}

