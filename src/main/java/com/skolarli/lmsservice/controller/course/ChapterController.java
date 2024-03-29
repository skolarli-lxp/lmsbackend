package com.skolarli.lmsservice.controller.course;

import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.models.db.course.Chapter;
import com.skolarli.lmsservice.models.dto.course.ChapterResponse;
import com.skolarli.lmsservice.models.dto.course.ChapterSortOrderRequest;
import com.skolarli.lmsservice.models.dto.course.ChapterSortOrderResponse;
import com.skolarli.lmsservice.models.dto.course.NewChapterRequest;
import com.skolarli.lmsservice.services.course.ChapterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

@RestController
@RequestMapping("/chapter")
public class ChapterController {
    private static final Logger logger = LoggerFactory.getLogger(ChapterController.class);

    final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllChapters(@RequestParam(required = false)
                                            Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getAllChapters");

        List<Chapter> response = null;
        List<ChapterResponse> condensedResponse = new ArrayList<>();

        try {
            response = chapterService.getAllChapters();
            if (condensed != null && condensed == Boolean.TRUE) {
                for (Chapter chapter : response) {
                    condensedResponse.add(new ChapterResponse(chapter));
                }
                return new ResponseEntity<>(condensedResponse, HttpStatus.OK);

            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Error in getAllChapters: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ResponseEntity<?> getChapter(@PathVariable long id,
                                        @RequestParam(required = false)
                                        Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getChapter with id: " + id);

        try {
            Chapter response = chapterService.getChapterById(id);
            if (condensed != null && condensed == Boolean.TRUE) {
                return new ResponseEntity<>(new ChapterResponse(response), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Error in getChapter: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "course/{id}")
    public ResponseEntity<?> getChaptersByCourseId(@PathVariable long id,
                                                   @RequestParam(required = false)
                                                   Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getChaptersByCourseId with id: " + id);

        List<Chapter> response = null;
        List<ChapterResponse> condensedResponse = new ArrayList<>();

        try {
            response = chapterService.getChaptersByCourseId(id);
            if (condensed != null && condensed == Boolean.TRUE) {
                for (Chapter chapter : response) {
                    condensedResponse.add(new ChapterResponse(chapter));
                }
                return new ResponseEntity<>(condensedResponse, HttpStatus.OK);

            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Error in getChaptersByCourseId: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "sortorder/{courseId}")
    public ResponseEntity<List<ChapterSortOrderResponse>> getChaptersSortOrder(
        @PathVariable long courseId) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for getChaptersSortOrder with courseId: " + courseId);

        try {
            return new ResponseEntity<>(chapterService.getChaptersSortOrder(courseId),
                HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getChaptersSortOrder: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addChapter(@RequestBody NewChapterRequest newChapterRequest,
                                        @RequestParam(required = false) Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for addChapter for course: "
            + newChapterRequest.getCourseId());

        Chapter chapter = chapterService.toChapter(newChapterRequest);
        try {
            Chapter response = chapterService.saveChapter(chapter);
            if (condensed != null && condensed == Boolean.TRUE) {
                return new ResponseEntity<>(new ChapterResponse(response), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Error in addChapter: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public ResponseEntity<?> updateChapter(@RequestBody NewChapterRequest newChapterRequest,
                                                 @PathVariable long id,
                                                 @RequestParam(required = false) Boolean condensed) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateChapter with id: " + id);

        try {
            Chapter response = chapterService.updateChapter(newChapterRequest, id);
            if (condensed != null && condensed == Boolean.TRUE) {
                return new ResponseEntity<>(new ChapterResponse(response), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Error in updateChapter: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "sortorder/{courseId}")
    public ResponseEntity<List<ChapterSortOrderResponse>> updateChaptersSortOrder(
        @Valid @RequestBody List<ChapterSortOrderRequest> chaptersSortOrder,
        @PathVariable long courseId) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for updateChaptersSortOrder with courseId: " + courseId);

        try {
            return new ResponseEntity<>(chapterService.updateChaptersSortOrder(courseId,
                chaptersSortOrder), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in updateChaptersSortOrder: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public ResponseEntity<Chapter> hardDeleteChapter(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for hardDeleteChapter with id: " + id);

        try {
            chapterService.hardDeleteChapter(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            logger.error("Error in hardDeleteChapter: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "soft/{id}")
    public ResponseEntity<Chapter> deleteChapter(@PathVariable long id) {

        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        logger.info("Received request for deleteChapter with id: " + id);

        try {
            chapterService.softDeleteChapter(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in deleteChapter: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            MDC.remove("requestId");
        }
    }
}