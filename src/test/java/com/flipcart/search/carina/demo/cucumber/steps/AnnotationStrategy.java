package com.flipcart.search.carina.demo.cucumber.steps;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class AnnotationStrategy<T> extends HeaderColumnNameTranslateMappingStrategy<T> {
    public AnnotationStrategy(Class<? extends T> clazz) {
        Map<String, String> map = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            CsvBindByName annotation = field.getAnnotation(CsvBindByName.class);
            if (annotation != null) {
                map.put(annotation.column(), annotation.column());
            }
        }
        setType(clazz);
        setColumnMapping(map);
    }

    @Override
    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
        String[] result = super.generateHeader(bean);
        for (int i = 0; i < result.length; i++) {
            result[i] = getColumnName(i);
        }
        return result;
    }
}