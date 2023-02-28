package com.kolay.pep.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchQueryDto extends QueryDto {

    private String firstName;
    private String lastName;
}
