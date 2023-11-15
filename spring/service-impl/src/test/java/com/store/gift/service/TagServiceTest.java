package com.store.gift.service;

import com.store.gift.dao.TagDao;
import com.store.gift.dao.TagDaoImpl;
import com.store.gift.dto.TagDto;
import com.store.gift.entity.Tag;
import com.store.gift.exception.TagAlreadyExistsException;
import com.store.gift.exception.TagNotFoundException;
import com.store.gift.mapper.TagMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TagServiceTest {
    @Mock
    private final TagDao tagDao = mock(TagDaoImpl.class);
    @Mock
    private TagMapper tagMapper = mock(TagMapper.class);
    @InjectMocks
    private final TagService tagService = new TagServiceImpl(tagDao, tagMapper);
    private List<Tag> tags;
    private List<TagDto> tagDtos;
    private final Long id = 1L;
    private final String tagName = "Spring";
    private final Tag tag = Tag.builder().id(id).name(tagName).build();
    private final TagDto tagDto = TagDto.builder().name(tagName).build();
    private final Pageable pageable = PageRequest.of(0, 25, Sort.by("name").ascending());

    @BeforeEach
    void setup() {
        tags = new ArrayList<>();
        tags.add(Tag.builder().id(1L).name("Tag1").build());
        tags.add(Tag.builder().id(2L).name("Tag2").build());
        tags.add(Tag.builder().id(3L).name("Tag3").build());
        tagDtos = new ArrayList<>();
        tagDtos.add(TagDto.builder().id(1L).name("Tag1").build());
        tagDtos.add(TagDto.builder().id(2L).name("Tag2").build());
        tagDtos.add(TagDto.builder().id(3L).name("Tag3").build());
    }

    @ParameterizedTest
    @MethodSource("existingTagNames")
    void testSaveTag(String tagName) {
        Tag tag = Tag.builder().name(tagName).build();
        TagDto tagDto = TagDto.builder().name(tagName).build();
        when(tagDao.save(any(Tag.class))).thenReturn(tag);
        when(tagMapper.toEntity(tagDto)).thenReturn(tag);
        when(tagMapper.toDto(tag)).thenReturn(tagDto);
        TagDto result = tagService.save(tagDto);
        verify(tagDao).save(tag);
        verify(tagMapper).toEntity(tagDto);
        verify(tagMapper).toDto(tag);
        assertEquals(tagDto, result);
    }

    private static Stream<Arguments> existingTagNames() {
        return Stream.of(
                Arguments.of("Winter"),
                Arguments.of("Spring"),
                Arguments.of("Summer"),
                Arguments.of("Autumn")
        );
    }


    @Test
    @DisplayName("Test creating TagAlreadyExistsException with message")
    void testCreateTagAlreadyExistsExceptionWithMessage() {
        String message = "Tag already exists";
        TagAlreadyExistsException exception = new TagAlreadyExistsException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("Should return all tags")
    void testGetAllShouldReturnAllTags() {
        when(tagDao.getAllBy(pageable)).thenReturn(tags);
        when(tagMapper.toDto(any(Tag.class))).thenReturn(
                TagDto.builder().id(tags.get(0).getId()).name(tags.get(0).getName()).build(),
                TagDto.builder().id(tags.get(1).getId()).name(tags.get(1).getName()).build(),
                TagDto.builder().id(tags.get(2).getId()).name(tags.get(2).getName()).build()
        );
        List<TagDto> actualTagDtos = tagService.getAll(pageable);
        IntStream.range(0, actualTagDtos.size()).forEach(i -> assertEquals(tagDtos.get(i), actualTagDtos.get(i)));
    }

    @Test
    @DisplayName("Should return all tags")
    void testGetAllShouldReturnAllTag() {
        when(tagDao.getAllBy(pageable)).thenReturn(tags);
        when(tagMapper.toDto(any())).thenReturn(tagDtos.get(0), tagDtos.get(1), tagDtos.get(2));
        assertEquals(tagDtos, tagService.getAll(pageable));
        verify(tagDao, times(1)).getAllBy(pageable);
        verify(tagMapper, times(3)).toDto(any());
    }

    @Test
    @DisplayName("Should return an empty list when there are no tags")
    void testGetAllWhenNoTags() {
        List<Tag> tags = Collections.emptyList();
        List<TagDto> tagDtos = Collections.emptyList();
        when(tagDao.getAllBy(pageable)).thenReturn(tags);

        List<TagDto> result = tagService.getAll(pageable);

        assertEquals(tagDtos, result);
        verify(tagDao, times(1)).getAllBy(pageable);
        verify(tagMapper, times(0)).toDto(any());
    }

    @ParameterizedTest
    @DisplayName("Should return empty list when no tags are found")
    @MethodSource("provideEmptyTagLists")
    void testGetAllShouldReturnEmptyList(List<Tag> emptyTagList) {
        when(tagDao.getAllBy(pageable)).thenReturn(emptyTagList);
        List<TagDto> actualTagDtos = tagService.getAll(pageable);
        assertTrue(actualTagDtos.isEmpty());
    }

    static Stream<Arguments> provideEmptyTagLists() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(Collections.emptyList())
        );
    }

    @Test
    @DisplayName("Should return an empty list when there are no tags")
    void testGetAllWhenNoTag() {
        List<Tag> tags = Collections.emptyList();
        List<TagDto> tagDtos = Collections.emptyList();
        when(tagDao.getAllBy(pageable)).thenReturn(tags);
        List<TagDto> result = tagService.getAll(pageable);

        assertEquals(tagDtos, result);
        verify(tagDao, times(1)).getAllBy(pageable);
        verify(tagMapper, times(0)).toDto(any());
    }

    @Test
    @DisplayName("Should return tag with specified id")
    void testGetByIdShouldReturnTag() {
        when(tagDao.getById(id)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(tag)).thenReturn(tagDto);
        TagDto actualTag = tagService.getById(id);
        assertEquals(tagDto, actualTag);
    }

    @Test
    @DisplayName("Should return tag with specified id")
    void testGetByIdShouldReturnTags() {
        when(tagDao.getById(id)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(tag)).thenReturn(tagDto);

        TagDto actual = tagService.getById(id);

        assertEquals(tagDto, actual);
    }

    @ParameterizedTest
    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})
    @DisplayName("Get tag by name")
    void getByName(Long id, String name) {
        TagDto tagDto = TagDto.builder()
                .id(id)
                .name(name)
                .build();
        Tag tag = Tag.builder()
                .id(id)
                .name(name)
                .build();
        when(tagDao.findByUsername(name)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(tag)).thenReturn(tagDto);
        assertEquals(tagDto, tagService.getByName(name));
        verify(tagDao, times(1)).findByUsername(name);
    }

    @Test
    @DisplayName("Delete tag by id")
    void testDelete() {
        when(tagDao.getById(id)).thenReturn(Optional.of(tag));
        tagService.delete(id);
        verify(tagDao).delete(id);
        verify(tagDao, times(1)).delete(id);
    }

    @Test
    @DisplayName("test getById returns TagDto when tag exists")
    void testGetByIdReturnsTagDtoWhenTagExists() {
        Tag tag = Tag.builder().id(id).name("test_tag").build();
        TagDto expectedTagDto = TagDto.builder().id(id).name("test_tag").build();
        when(tagDao.getById(id)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(tag)).thenReturn(expectedTagDto);
        TagDto actualTagDto = tagService.getById(id);
        assertNotNull(actualTagDto);
        assertEquals(expectedTagDto, actualTagDto);
        verify(tagDao).getById(id);
        verify(tagMapper).toDto(tag);
    }

    @Test
    @DisplayName("Given a non-existing tag, when getById is called, then TagNotFoundException should be thrown")
    void testGetByIdThrowsTagNotFoundExceptionWhenTagDoesNotExist() {
        when(tagDao.getById(id)).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.getById(id));
        verify(tagDao).getById(id);
    }

    @DisplayName("getByName() method should return the tag with the given name")
    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})
    @ParameterizedTest
    void getByNameShouldReturnTag(final Long tagId, final String name) {
        final Tag tag = Tag.builder().id(tagId).name(name).build();
        final TagDto tagDto = TagDto.builder().id(tagId).name(name).build();

        when(tagDao.findByUsername(name)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(tag)).thenReturn(tagDto);

        final TagDto result = tagService.getByName(name);

        assertNotNull(result);
        assertEquals(tagDto.getId(), result.getId());
        assertEquals(tagDto.getName(), result.getName());

        verify(tagDao).findByUsername(name);
        verify(tagMapper).toDto(tag);
    }

    @DisplayName("getByName() method should throw TagNotFoundException if tag with the given name not found")
    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})
    @ParameterizedTest
    void getByNameShouldThrowTagNotFoundException(final long id, final String name) {
        when(tagDao.findByUsername(name)).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.getByName(name));
        verify(tagDao).findByUsername(name);

        when(tagDao.getById(id)).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.getById(id));
        verify(tagDao).getById(id);
    }
}
