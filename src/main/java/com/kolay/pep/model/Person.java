package com.kolay.pep.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@CompoundIndexes({
        @CompoundIndex(name = "firstName_lastName", def = "{'firstName' : 1, 'lastName': 1}")
})
public class Person {
    @Id
    private String id;
    private String typeOfOfficial;
    @Indexed
    private String firstName;
    @Indexed
    private String lastName;
    @Indexed
    private Boolean isPep;
    private String fullNameEn;
    private String firstNameEn;
    private String lastNameEn;
    private String url;
    private String dateOfBirth;
    private String typeOfOfficialEn;
    private String fullName;
    private String patronymic;
    private String patronymicEn;
    private Boolean died;
    private String alsoKnownAsEn;
    private String names;
}
