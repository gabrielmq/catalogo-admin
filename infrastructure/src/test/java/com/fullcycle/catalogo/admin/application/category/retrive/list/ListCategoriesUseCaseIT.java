package com.fullcycle.catalogo.admin.application.category.retrive.list;

import com.fullcycle.catalogo.admin.IntegrationTest;
import com.fullcycle.catalogo.admin.application.category.retrieve.list.ListCategoriesUseCase;
import com.fullcycle.catalogo.admin.domain.category.Category;
import com.fullcycle.catalogo.admin.domain.category.CategorySearchQuery;
import com.fullcycle.catalogo.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.catalogo.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class ListCategoriesUseCaseIT {

    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void mockUp() {
        final var categories =
            Stream.of(
                Category.newCategoryWith("Filmes", null, true),
                Category.newCategoryWith("Netflix Originals", "Títulos de autoria da Netflix", true),
                Category.newCategoryWith("Amazon Originals", "Títulos de autoria da Amazon Prime", true),
                Category.newCategoryWith("Documentários", null, true),
                Category.newCategoryWith("Sports", null, true),
                Category.newCategoryWith("Kids", "Categoria para crianças", true),
                Category.newCategoryWith("Series", null, true)
            )
            .map(CategoryJpaEntity::from)
            .toList();

        categoryRepository.saveAllAndFlush(categories);
    }

    @Test
    public void givenAValidTerm_whenTermDoesntMatchsPrePersisted_thenShouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ji1j3i 1j3i1oj";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery =
            new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
        "fil,0,10,1,1,Filmes",
        "net,0,10,1,1,Netflix Originals",
        "ZON,0,10,1,1,Amazon Originals",
        "KI,0,10,1,1,Kids",
        "crianças,0,10,1,1,Kids",
        "da amazon,0,10,1,1,Amazon Originals",
    })
    public void givenAValidTerm_whenCallsListCategories_thenShouldReturnCategoriesFiltered(
        final String expectedTerms,
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final int expectedTotal,
        final String expectedCategoryName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
        "name,asc,0,10,7,7,Amazon Originals",
        "name,desc,0,10,7,7,Sports",
        "createdAt,asc,0,10,7,7,Filmes",
        "createdAt,desc,0,10,7,7,Series",
    })
    public void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(
        final String expectedSort,
        final String expectedDirection,
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final int expectedTotal,
        final String expectedCategoryName
    ) {
        final var expectedTerms = "";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
        "0,2,2,7,Amazon Originals;Documentários",
        "1,2,2,7,Filmes;Kids",
        "2,2,2,7,Netflix Originals;Series",
        "3,2,1,7,Sports",
    })
    public void givenAValidPage_whenCallsListCategories_thenShouldReturnCategoriesPaginated(
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final int expectedTotal,
        final String expectedCategoriesName
    ) {
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for (String expectedName : expectedCategoriesName.split(";")) {
            assertEquals(expectedName, actualResult.items().get(index).name());
            index++;
        }
    }
}