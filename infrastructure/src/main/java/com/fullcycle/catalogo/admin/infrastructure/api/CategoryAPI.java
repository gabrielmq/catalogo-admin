package com.fullcycle.catalogo.admin.infrastructure.api;

import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CategoryListResponse;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CategoryResponse;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.catalogo.admin.infrastructure.category.models.UpdateCategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Categories")
@RequestMapping("categories")
public interface CategoryAPI {
    @PostMapping(
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new Category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest input);

    @GetMapping
    @Operation(summary = "List all categories paginated")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listed successfully"),
        @ApiResponse(responseCode = "400", description = "A invalid parameter was received"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    Pagination<CategoryListResponse> listCategories(
        @RequestParam(name = "search", required = false, defaultValue = "") String search,
        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
        @RequestParam(name = "perPage", required = false, defaultValue = "10") int perPage,
        @RequestParam(name = "sort", required = false, defaultValue = "name") String sort,
        @RequestParam(name = "dir", required = false, defaultValue = "asc") String direction
    );

    @GetMapping(
        value = "{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get an category by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category retrieve successfully"),
        @ApiResponse(responseCode = "404", description = "Category was not found"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<CategoryResponse> getById(@PathVariable String id);

    @PutMapping(
        value = "{id}",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a category by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully"),
        @ApiResponse(responseCode = "404", description = "Category was not found"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> updateById(@PathVariable String id, @RequestBody UpdateCategoryRequest input);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Delete a category by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    void deleteById(@PathVariable String id);
}
