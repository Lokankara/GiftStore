package com.store.gift.assembler;

import com.store.gift.controller.TagController;
import com.store.gift.dto.TagDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
class TagAssemblerTest {
    private final TagAssembler tagAssembler = new TagAssembler();

    @ParameterizedTest
    @DisplayName("Test toCollectionModel method of TagAssembler")
    @CsvSource({
            "1, Winter",
            "2, Summer",
            "3, Spring",
            "4, Autumn"
    })
    void testToCollectionModel(Long id, String name) {
        List<TagDto> tagDtos = Arrays.asList(
                TagDto.builder().id(id).build(),
                TagDto.builder().name(name).build(),
                TagDto.builder().id(id).name(name).build()
        );
        List<EntityModel<TagDto>> expectedModels = tagDtos.stream()
                .map(tagDto -> EntityModel.of(tagDto,
                        linkTo(methodOn(TagController.class)
                                .getById(tagDto.getId())).withSelfRel(),
                        linkTo(methodOn(TagController.class)
                                .getAll(PageRequest.of(
                                        0, 25,
                                        Sort.by("id").ascending())))
                                .withRel("tags"),
                        linkTo(methodOn(TagController.class)
                                .delete(tagDto.getId()))
                                .withRel("delete")))
                .collect(toList());

        CollectionModel<EntityModel<TagDto>> actualCollectionModel = tagAssembler.toCollectionModel(tagDtos);
        assertEquals(expectedModels.size(), actualCollectionModel.getContent().size());
        List<EntityModel<TagDto>> actualModels = new ArrayList<>(actualCollectionModel.getContent());
        assertEquals(expectedModels.get(0).getContent(), actualModels.get(0).getContent());
        assertEquals(expectedModels.get(1).getContent(), actualModels.get(1).getContent());
        assertEquals(expectedModels.get(2).getContent(), actualModels.get(2).getContent());
    }
}
