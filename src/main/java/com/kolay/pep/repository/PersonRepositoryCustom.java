package com.kolay.pep.repository;

import com.kolay.pep.dto.SearchQueryDto;
import com.kolay.pep.dto.StatisticDto;
import com.kolay.pep.model.Person;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PersonRepositoryCustom {
    Page<Person> search(SearchQueryDto searchQueryDto);
    List<StatisticDto> getTopNames();
}
