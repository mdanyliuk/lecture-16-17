package com.kolay.pep.controller;

import com.kolay.pep.dto.PageDto;
import com.kolay.pep.dto.PersonInfoDto;
import com.kolay.pep.dto.SearchQueryDto;
import com.kolay.pep.dto.StatisticDto;
import com.kolay.pep.service.PepServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/pep")
public class PepController {

    @Autowired
    PepServiceImpl pepService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        pepService.handleFile(multipartFile);
        return ResponseEntity.ok().body("File uploaded successfully");
    }

    @PostMapping("_search")
    public PageDto<PersonInfoDto> search(@RequestBody SearchQueryDto query) {
        return pepService.search(query);
    }

    @GetMapping("/topNames")
    public List<StatisticDto> getTopNames() {
        return pepService.getTopNames();
    }
}
