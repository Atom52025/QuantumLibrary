package quantum.utils;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class LongListConverter implements AttributeConverter<List<Long>, String> {

    @Override
    public String convertToDatabaseColumn(List<Long> longList) {
        return longList != null ? longList.stream().map(String::valueOf).collect(Collectors.joining(",")) : "";
    }

    @Override
    public List<Long> convertToEntityAttribute(String string) {
        return string != null && !string.isEmpty() ?
                Arrays.stream(string.split(",")).map(Long::valueOf).collect(Collectors.toList()) :
                List.of();
    }
}
