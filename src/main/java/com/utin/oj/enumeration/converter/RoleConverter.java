package com.utin.oj.enumeration.converter;

import com.utin.oj.enumeration.Authority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;

import java.util.stream.Stream;
@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Authority,String> {
    @Override
    public String convertToDatabaseColumn(Authority authority) {
        if(authority == null){
            return null;
        }
        else return authority.getValue();
    }

    @Override
    public Authority convertToEntityAttribute(String code) {
        if(code == null){
            return null;
        }
        return Stream.of(Authority.values())
                .filter(authority -> authority.getValue().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
