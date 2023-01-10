package com.fullcycle.catalogo.admin.infrastructure.api;

import com.fullcycle.catalogo.admin.infrastructure.video.models.CreateVideoRequest;
import com.fullcycle.catalogo.admin.infrastructure.video.models.UpdateVideoRequest;
import com.fullcycle.catalogo.admin.infrastructure.video.models.VideoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "Video")
@RequestMapping(value = "videos")
public interface VideoAPI {
    @PostMapping(
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new video with medias")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> createFull(
        @RequestParam(name = "title", required = false) String title,
        @RequestParam(name = "description", required = false) String description,
        @RequestParam(name = "year_launched", required = false) Integer yearLaunched,
        @RequestParam(name = "duration", required = false) Double duration,
        @RequestParam(name = "opened", required = false) Boolean opened,
        @RequestParam(name = "published", required = false) Boolean published,
        @RequestParam(name = "rating", required = false) String rating,
        @RequestParam(name = "categories_id", required = false) Set<String> categories,
        @RequestParam(name = "genres_id", required = false) Set<String> genres,
        @RequestParam(name = "cast_members_id", required = false) Set<String> members,
        @RequestParam(name = "video_file", required = false) MultipartFile videoFile,
        @RequestParam(name = "trailer_file", required = false) MultipartFile trailerFile,
        @RequestParam(name = "banner_file", required = false) MultipartFile bannerFile,
        @RequestParam(name = "thumb_file", required = false) MultipartFile thumbFile,
        @RequestParam(name = "thumb_half_file", required = false) MultipartFile thumbHalfFile
    );

    @PostMapping(
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new video without medias")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> createPartial(@RequestBody CreateVideoRequest aRequest);

    @GetMapping(
        value = "{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a video by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Video retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Video was not found"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    VideoResponse getById(@PathVariable(name = "id") String anId);

    @PutMapping(
        value = "{id}",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a video by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Video updated successfully"),
        @ApiResponse(responseCode = "404", description = "Video was not found"),
        @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> updateById(@PathVariable(name = "id") String anId, @RequestBody UpdateVideoRequest aRequest);

    @DeleteMapping(
        value = "{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Delete a video by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Video deleted"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    void deleteById(@PathVariable(name = "id") String anId);
}
