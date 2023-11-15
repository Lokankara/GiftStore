package com.store.gift.controller;

import com.store.gift.dto.TagDto;
import com.store.gift.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
class TagControllerTest {
    String admin = "ROLE_ADMIN";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    long id = 1L;
    String tagName = "Summer";
    long id2 = 2L;
    String tagName2 = "Winter";
    TagDto tagDto = TagDto.builder().id(id).name(tagName).build();

    @Test
    @DisplayName("Test getById - Retrieves a tag by ID")
    void getByIdTest() throws Exception {

        given(tagService.getById(id)).willReturn(tagDto);

        mockMvc.perform(get("/tags/{id}", id)
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(tagName));
    }

    @Test
    @DisplayName("Test getAll - Retrieves all tags with pagination")
    void getAllTest() throws Exception {

        TagDto tagDto1 = TagDto.builder()
                .id(id)
                .name(tagName)
                .build();
        TagDto tagDto2 = TagDto.builder()
                .id(id2)
                .name(tagName2)
                .build();
        List<TagDto> tagDtoList = Arrays.asList(tagDto, tagDto1, tagDto2);
        given(tagService.getAll(any())).willReturn(tagDtoList);

        mockMvc.perform(get("/tags")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.tagDtoList[0].id").value(id))
                .andExpect(jsonPath("$._embedded.tagDtoList[0].name").value(tagName));
    }

    @Test
    @DisplayName("Create Test - Verify successful creation of a tag")
    void createTest() throws Exception {

        given(tagService.save(tagDto)).willReturn(tagDto);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/tags")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(tagName));
    }

    @Test
    @DisplayName("Test delete - Deletes a tag by ID")
    void deleteTest() throws Exception {
        mockMvc.perform(delete("/tags/{id}", id)
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isNoContent());
        verify(tagService, times(1)).delete(id);
    }
}