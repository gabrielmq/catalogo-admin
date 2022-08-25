package com.fullcycle.catalogo.admin.infrastructure.api;

import com.fullcycle.catalogo.admin.infrastructure.castmember.models.CastMemberResponse;
import com.fullcycle.catalogo.admin.infrastructure.castmember.models.CreateCastMemberRequest;
import com.fullcycle.catalogo.admin.infrastructure.castmember.models.UpdateCastMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Cast Members")
@RequestMapping("cast_members")
public interface CastMemberAPI {

    @PostMapping(
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new cast member")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> create(@RequestBody CreateCastMemberRequest input);

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a cast member by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cast member retrieved"),
        @ApiResponse(responseCode = "404", description = "Cast member was not found"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<CastMemberResponse> getById(@PathVariable String id);

    @PutMapping(
        value = "{id}",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a cast member by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cast member updated"),
        @ApiResponse(responseCode = "404", description = "Cast member was not found"),
        @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> updateById(@PathVariable String id, @RequestBody UpdateCastMemberRequest aBody);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Delete a cast member by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cast member deleted"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    void deleteById(@PathVariable String id);
}
