package com.kolay.pep.repository;

import com.kolay.pep.dto.SearchQueryDto;
import com.kolay.pep.dto.StatisticDto;
import com.kolay.pep.model.Person;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class PersonRepositoryCustomImpl implements PersonRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Person> search(SearchQueryDto searchQueryDto) {
        PageRequest pageRequest = PageRequest.of(
                searchQueryDto.getPage(),
                searchQueryDto.getSize(),
                Sort.by(Sort.Direction.ASC, Person.Fields.id)
        );
        Query mongoQuery = new Query().with(pageRequest);
        if (StringUtils.isNotBlank(searchQueryDto.getFirstName())) {
            mongoQuery.addCriteria(where(Person.Fields.firstName).is(searchQueryDto.getFirstName()));
        }
        if (StringUtils.isNotBlank(searchQueryDto.getLastName())) {
            mongoQuery.addCriteria(where(Person.Fields.lastName).is(searchQueryDto.getLastName()));
        }

        final List<Person> persons = mongoTemplate.find(mongoQuery, Person.class);

        return PageableExecutionUtils.getPage(
                persons,
                pageRequest,
                () -> mongoTemplate.count((Query.of(mongoQuery).limit(-1).skip(-1)), Person.class)
        );
    }

    @Override
    public List<StatisticDto> getTopNames() {
        MatchOperation filterPep = match(new Criteria("isPep").is(true));
        GroupOperation countNames = group("firstName").count().as("countName");
        SortOperation sortByCount = sort(Direction.DESC, "countName");
        LimitOperation limitToTen = limit(10);
        ProjectionOperation projectToMatchModel = project()
                .andExpression("_id").as("firstName")
                .andExpression("countName").as("count");

        Aggregation aggregation = newAggregation(filterPep, countNames, sortByCount, limitToTen, projectToMatchModel);

        AggregationResults<StatisticDto> result = mongoTemplate
                .aggregate(aggregation, mongoTemplate.getCollectionName(Person.class), StatisticDto.class);
        return result.getMappedResults();
    }
}
