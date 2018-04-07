package com.infoDiscover.adminCenter.logic.common;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;

/**
 * Created by wangychu on 3/16/17.
 */
public class CustomizedConverterFactory extends DefaultConverterFactory {
    @Override
    protected <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> findConverter(
            Class<PRESENTATION> presentationType, Class<MODEL> modelType) {
        // Handle String <-> Double
        if (presentationType == String.class && modelType == Double.class) {
            return (Converter<PRESENTATION, MODEL>) new HighFractionDigitsStringToDoubleConverter();
        }
        // Handle String <-> Float
        if (presentationType == String.class && modelType == Float.class) {
            return (Converter<PRESENTATION, MODEL>) new HighFractionDigitsStringToFloatConverter();
        }
        // Let default factory handle the rest
        return super.findConverter(presentationType, modelType);
    }
}
