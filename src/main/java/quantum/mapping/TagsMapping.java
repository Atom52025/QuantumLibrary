package quantum.mapping;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import quantum.dto.common.DataResponseTags;

/**
 * Mapper for {@link DataResponseTags}.
 */
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TagsMapping {

    /**
     * Map {@link String} to {@link DataResponseTags}.
     *
     * @param tags The concatenated on a string.
     * @return The converted element.
     */
    @Mapping(expression = "java(java.util.Arrays.asList(tags.split(\", \")))", target = "tags")
    DataResponseTags map(String tags);
}
