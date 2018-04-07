package com.infoDiscover.adminCenter.logic.common;

import com.vaadin.data.util.converter.StringToDoubleConverter;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by wangychu on 3/16/17.
 */
public class HighFractionDigitsStringToDoubleConverter extends StringToDoubleConverter {

    @Override
    protected NumberFormat getFormat(Locale locale) {
        NumberFormat format = super.getFormat(locale);
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(7);
        format.setMinimumFractionDigits(1);
        return format;
    }
}
