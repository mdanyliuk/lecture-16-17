package com.kolay.pep.service;

import com.kolay.pep.dto.PageDto;
import com.kolay.pep.dto.PersonInfoDto;
import com.kolay.pep.dto.SearchQueryDto;
import com.kolay.pep.dto.StatisticDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PepService {

    void handleFile(MultipartFile multipartFile);
    PageDto<PersonInfoDto> search(SearchQueryDto query);
    List<StatisticDto> getTopNames();
}
