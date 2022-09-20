package com.fullcycle.catalogo.admin.application.castmember.retrive.list;

import com.fullcycle.catalogo.admin.application.Fixture;
import com.fullcycle.catalogo.admin.application.UseCaseTest;
import com.fullcycle.catalogo.admin.domain.castmember.CastMember;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListCastMembersUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultListCastMembersUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembers_thenShouldReturnAll() {
        final var members = List.of(
            CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()),
            CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = members.stream().map(CastMemberListOutput::from).toList();
        final var expectedPagination = new Pagination<>(
            expectedPage,
            expectedPerPage,
            expectedTotal,
            members
        );

        when(castMemberGateway.findAll(any())).thenReturn(expectedPagination);

        final var aQuery = new SearchQuery(
            expectedPage,
            expectedPerPage,
            expectedTerms,
            expectedSort,
            expectedDirection
        );

        final var actualOutput = useCase.execute(aQuery);

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(castMemberGateway).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndIsEmpty_thenShouldReturn() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<CastMemberListOutput>of();
        final var expectedPagination = new Pagination<>(
            expectedPage,
            expectedPerPage,
            expectedTotal,
            List.<CastMember>of()
        );

        when(castMemberGateway.findAll(any())).thenReturn(expectedPagination);

        final var aQuery = new SearchQuery(
            expectedPage,
            expectedPerPage,
            expectedTerms,
            expectedSort,
            expectedDirection
        );

        final var actualOutput = useCase.execute(aQuery);

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(castMemberGateway).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndGatewayThrowsRandomException_thenShouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        when(castMemberGateway.findAll(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aQuery = new SearchQuery(
            expectedPage,
            expectedPerPage,
            expectedTerms,
            expectedSort,
            expectedDirection
        );

        final var actualException =
            assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findAll(eq(aQuery));
    }
}
