package com.kolay.pep.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class PersonInfoDto {

    private String firstName;
    private String patronymic;
    private String lastName;
    private Boolean isPep;
}
