package com.kolay.pep.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.kolay.pep.dto.PageDto;
import com.kolay.pep.dto.PersonInfoDto;
import com.kolay.pep.dto.SearchQueryDto;
import com.kolay.pep.dto.StatisticDto;
import com.kolay.pep.model.Person;
import com.kolay.pep.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class PepServiceImpl implements PepService {

    @Autowired
    PersonRepository personRepository;

    @Override
    public void handleFile(MultipartFile multipartFile) {
        String folder = "files-upload";
        File jsonFile = this.uploadFile(multipartFile, folder);
        this.parseJsonFile(jsonFile);
    }

    @Override
    public PageDto<PersonInfoDto> search(SearchQueryDto query) {
        Page<Person> page = personRepository.search(query);
        return PageDto.fromPage(page, this::toInfoDto);
    }

    @Override
    public List<StatisticDto> getTopNames() {
        return personRepository.getTopNames();
    }

    private File uploadFile(MultipartFile multipartFile, String folder) {
        File directory = new File(folder);
        File newFile;
        if (!directory.exists()){
            directory.mkdir();
        }
        String fileName = multipartFile.getOriginalFilename();
        Path uploadPath = Paths.get(folder);
        Path filePath = uploadPath.resolve(fileName);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] buffer = new byte[1024];
        String fileZip = folder + "/" + fileName;
        try(ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip))) {
            ZipEntry zipEntry = zis.getNextEntry();
            if (zipEntry == null) {
                throw new IllegalArgumentException("Wrong file: should be not empty ZIP-archive");
            }
            if (zipEntry.isDirectory()) {
                throw new IllegalArgumentException("Wrong file: ZIP-archive should not contains a folder");
            }
            newFile = new File(folder, zipEntry.getName());
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return newFile;
    }

    private void parseJsonFile(File jsonFile) {
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonParser jsonParser = jsonFactory.createParser(jsonFile)) {
            if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("An array is expected");
            }
            personRepository.deleteAll();
            Person person = new Person();
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                if (jsonParser.currentToken() == JsonToken.START_OBJECT) {
                    person = new Person();
                } else if (jsonParser.currentToken() == JsonToken.END_OBJECT) {
                    personRepository.save(person);
                } else {
                    String property = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    switch (property) {
                        case "type_of_official" -> person.setTypeOfOfficial(jsonParser.getText());
                        case "first_name" -> person.setFirstName(jsonParser.getText());
                        case "last_name" -> person.setLastName(jsonParser.getText());
                        case "full_name_en" -> person.setFullNameEn(jsonParser.getText());
                        case "first_name_en" -> person.setFirstNameEn(jsonParser.getText());
                        case "last_name_en" -> person.setLastNameEn(jsonParser.getText());
                        case "url" -> person.setUrl(jsonParser.getText());
                        case "date_of_birth" -> person.setDateOfBirth(jsonParser.getText());
                        case "type_of_official_en" -> person.setTypeOfOfficialEn(jsonParser.getText());
                        case "full_name" -> person.setFullName(jsonParser.getText());
                        case "patronymic" -> person.setPatronymic(jsonParser.getText());
                        case "patronymic_en" -> person.setPatronymicEn(jsonParser.getText());
                        case "also_known_as_en" -> person.setAlsoKnownAsEn(jsonParser.getText());
                        case "names" -> person.setNames(jsonParser.getText());
                        case "is_pep" -> person.setIsPep(jsonParser.getBooleanValue());
                        case "died" -> person.setDied(jsonParser.getBooleanValue());
                        case "related_persons", "related_companies", "declarations", "related_countries" -> {
                            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {

                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
        jsonFile.delete();
    }

    private PersonInfoDto toInfoDto(Person data) {
        return PersonInfoDto.builder()
                .firstName(data.getFirstName())
                .patronymic(data.getPatronymic())
                .lastName(data.getLastName())
                .isPep(data.getIsPep())
                .build();
    }
}
