package com.infoDiscover.adminCenter.logic.common;

import com.vaadin.data.util.converter.StringToFloatConverter;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by wangychu on 3/16/17.
 */
public class HighFractionDigitsStringToFloatConverter extends StringToFloatConverter {

    @Override
    protected NumberFormat getFormat(Locale locale) {
        NumberFormat format = super.getFormat(locale);
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(5);
        format.setMinimumFractionDigits(1);
        return format;
    }
}
