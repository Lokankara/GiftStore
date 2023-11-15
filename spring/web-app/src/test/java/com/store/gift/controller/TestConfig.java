package com.store.gift.controller;

import com.store.gift.assembler.CertificateAssembler;
import com.store.gift.assembler.OrderAssembler;
import com.store.gift.assembler.TagAssembler;
import com.store.gift.assembler.UserAssembler;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@TestConfiguration
public class TestConfig {

    @Bean
    public CertificateAssembler certificateAssembler() {
        return new CertificateAssembler();
    }

    @Bean
    public TagAssembler tagAssembler() {
        return new TagAssembler();
    }

    @Bean
    public UserAssembler userAssembler() {
        return new UserAssembler();
    }

    @Bean
    public OrderAssembler orderAssembler() {
        return new OrderAssembler();
    }

    public static MockHttpServletRequestBuilder withAuthorities(
            MockHttpServletRequestBuilder builder, String... authorities) {
        return builder.with(jwt().authorities(Arrays.stream(authorities)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())));
    }

    @NotNull
    public static List<MockHttpServletRequestBuilder> getCrudHttpRequests(String host) {
        return Arrays.asList(get(host), post(host), put(host), delete(host));
    }
}