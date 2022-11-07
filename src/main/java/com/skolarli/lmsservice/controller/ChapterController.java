package com.skolarli.lmsservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.skolarli.lmsservice.models.NewChapterRequest;
import com.skolarli.lmsservice.models.db.Chapter;
import com.skolarli.lmsservice.services.ChapterService;

@RestController
@RequestMapping("/chapter")
public class ChapterController {
    private static final Logger logger = LoggerFactory.getLogger(ChapterController.class);

    ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Chapter>> getAllChapters() {
        try {
            return new ResponseEntity<>(chapterService.getAllChapters(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getAllChapters: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ResponseEntity<Chapter> getChapter(@PathVariable long id) {
        try {
            return new ResponseEntity<>(chapterService.getChapterById(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getChapter: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Chapter> addChapter(@RequestBody NewChapterRequest newChapterRequest) {
        Chapter chapter = chapterService.toChapter(newChapterRequest);
        try {
            return new ResponseEntity<>(chapterService.saveChapter(chapter), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in addChapter: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public ResponseEntity<Chapter> updateChapter(@RequestBody NewChapterRequest newChapterRequest, @PathVariable long id) {
        Chapter chapter = chapterService.toChapter(newChapterRequest);
        try {
            return new ResponseEntity<>(chapterService.updateChapter(chapter, id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in updateChapter: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public ResponseEntity<Chapter> deleteChapter(@PathVariable long id) {
        try {
            chapterService.deleteChapter(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in deleteChapter: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "hard/{id}")
    public ResponseEntity<Chapter> hardDeleteChapter(@PathVariable long id) {
        try {
            chapterService.hardDeleteChapter(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in hardDeleteChapter: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}