package com.store.gift.mapper;

import com.store.gift.dto.CertificateDto;
import com.store.gift.dto.CertificateSlimDto;
import com.store.gift.dto.PatchCertificateDto;
import com.store.gift.entity.Certificate;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper interface for converting
 * between Certificate and CertificateDto objects.
 */
@Mapper(componentModel = "spring",
        uses = {TagMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CertificateMapper {

    /**
     * Converts a CertificateDto object to a Certificate entity.
     *
     * @param dto the CertificateDto object to convert
     * @return the converted Certificate entity
     */
    @Mapping(target = "tags", source = "tags",
            qualifiedByName = "toTagSet")
    Certificate toEntity(CertificateDto dto);
    @Mapping(target = "tags", source = "tags",
            qualifiedByName = "toTagSet")
    Certificate toEntity(PatchCertificateDto dto);

    /**
     * Converts a Certificate entity to a CertificateDto object.
     *
     * @param certificate the Certificate entity to convert
     * @return the converted CertificateDto object
     */
    @Mapping(target = "tags", source = "tags",
            qualifiedByName = "toTagDtoSet")
    CertificateDto toDto(Certificate certificate);

    /**
     * Converts a list of Certificate entities
     * to a list of CertificateDto objects.
     *
     * @param certificates the list of Certificate entities
     * @return the converted list of CertificateDto objects
     */
    @Mapping(target = "tags", source = "tags",
            qualifiedByName = "toTagDtoSet")
    List<CertificateDto> toDtoList(List<Certificate> certificates);

    /**
     * Converts a list of CertificateDto objects
     * to a list of Certificate entities.
     *
     * @param dtos the list of CertificateDto objects
     * @return the converted list of Certificate entities
     */
    @Mapping(target = "tags", source = "tags",
            qualifiedByName = "toTagSet")
    List<Certificate> toListEntity(List<CertificateDto> dtos);

    /**
     * Converts a list of Certificate entities
     * to a list of CertificateSlimDto objects.
     *
     * @param list the list of Certificate entities to convert
     * @return the converted list of CertificateSlimDto objects
     */
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "orderDtos", ignore = true)
    List<CertificateSlimDto> toCertificateSlimDto(List<Certificate> list);

    /**
     * Converts a CertificateSlimDto object to a Certificate entity.
     *
     * @param certificateSlimDto the CertificateSlimDto object
     * @return the converted Certificate entity
     */
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "orders", ignore = true)
    Certificate toDto(CertificateSlimDto certificateSlimDto);

    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "tags", source = "tags", ignore = true)
    List<CertificateDto> toDtoListFromSlim(List<CertificateSlimDto> list);
}

