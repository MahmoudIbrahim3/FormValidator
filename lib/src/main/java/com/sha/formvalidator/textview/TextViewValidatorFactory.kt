package com.sha.formvalidator.textview

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import com.annimon.stream.Stream
import com.sha.formvalidator.R
import com.sha.formvalidator.textview.validator.*
import com.sha.formvalidator.textview.validator.pattern.*

object TextViewValidatorFactory {

    fun validator(
            attrInfo: TextViewAttrInfo,
            context: Context
    ): Validator {

        val validator = when (attrInfo.validationType) {
            TextViewValidationType.NOT_DETECTABLE -> {
                if (!TextUtils.isEmpty(attrInfo.customValidationType))
                    customValidator(attrInfo, context) else
                    predefinedValidator(attrInfo, context)
            }

            else -> predefinedValidator(attrInfo, context)
        }

        if (!TextUtils.isEmpty(attrInfo.errorMessage)) validator.errorMessage = attrInfo.errorMessage

        // If the xml tells us that this is not a required field, we will add new InverseValidator(new RequiredValidator()).
        return if (attrInfo.required) ValidatorFactory.allValid(
                RequiredValidator(attrInfo.emptyErrorMessage(context)),
                validator
        ) else ValidatorFactory.anyValid(
                validator.errorMessage,
                InverseValidator(RequiredValidator()),
                validator
        )

    }

    private fun customValidator(attrInfo: TextViewAttrInfo, context: Context): Validator {
        val opt = Stream.of(TextViewValidators.customValidators)
                .filter { item -> item.customValidationType(context) == attrInfo.customValidationType }
                .findFirst()

        check(opt.isPresent) { "couldn't find a custom validator for custom validation type: ${attrInfo.customValidationType}" }

        return opt.get()
    }

    @SuppressLint("StringFormatMatches")
    private fun predefinedValidator(attrInfo: TextViewAttrInfo, context: Context): Validator {
        return when (attrInfo.validationType) {
            TextViewValidationType.NOT_EMPTY -> DummyValidator()

            TextViewValidationType.ALPHA -> AlphaValidator(context.getString(R.string.error_only_standard_letters_are_allowed))

            TextViewValidationType.ALPHA_NUMERIC -> AlphaNumericValidator(context.getString(R.string.error_this_field_cannot_contain_special_character))

            TextViewValidationType.NUMERIC -> NumericValidator(context.getString(R.string.error_only_numeric_digits_allowed))

            TextViewValidationType.NUMERIC_RANGE -> NumericRangeValidator(
                    context.getString(R.string.error_only_numeric_digits_range_allowed, attrInfo.minNumber, attrInfo.maxNumber),
                    attrInfo.minNumber.toLong(),
                    attrInfo.maxNumber.toLong()
            )

            TextViewValidationType.FLOAT_NUMERIC_RANGE -> FloatNumericRangeValidator(
                    context.getString(R.string.error_only_numeric_digits_range_allowed, attrInfo.floatMinNumber, attrInfo.floatMaxNumber),
                    attrInfo.floatMinNumber.toDouble(),
                    attrInfo.floatMaxNumber.toDouble()
            )

            TextViewValidationType.REGEX -> PatternValidator(attrInfo.errorMessage, attrInfo.regex)

            TextViewValidationType.CREDIT_CARD -> CreditCardValidator(context.getString(R.string.error_credit_card_number_not_valid))

            TextViewValidationType.EMAIL -> EmailValidator(context.getString(R.string.error_email_address_not_valid))

            TextViewValidationType.PHONE -> PhoneValidator(context.getString(R.string.error_phone_not_valid))

            TextViewValidationType.DOMAIN_NAME -> DomainValidator(context.getString(R.string.error_domain_not_valid))

            TextViewValidationType.IP_ADDRESS -> IpAddressValidator(context.getString(R.string.error_ip_not_valid))

            TextViewValidationType.WEB_URL -> WebUrlValidator(context.getString(R.string.error_url_not_valid))

            TextViewValidationType.PERSON_NAME -> PersonNameValidator(context.getString(R.string.error_not_valid_person_name))

            TextViewValidationType.PERSON_FULL_NAME -> PersonFullNameValidator(context.getString(R.string.error_not_valid_person_full_name))

            TextViewValidationType.DATE -> DateValidator(context.getString(R.string.error_date_not_valid), attrInfo.dateFormat)

            else -> DummyValidator()
        }
    }

}
