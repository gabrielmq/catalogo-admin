package com.fullcycle.catalogo.admin.application.category.delete;

import com.fullcycle.catalogo.admin.application.UseCaseTest;
import com.fullcycle.catalogo.admin.domain.category.Category;
import com.fullcycle.catalogo.admin.domain.category.CategoryGateway;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteCategoryUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    public void givenAValidID_whenCallsDeleteCategory_thenShouldBeOK() {
        final var aCategory = Category.newCategoryWith("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        doNothing().when(categoryGateway).deleteById(eq(expectedId));

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAInvalidID_whenCallsDeleteCategory_thenShouldBeOK() {
        final var expectedId = CategoryID.from("123");

        doNothing().when(categoryGateway).deleteById(eq(expectedId));

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAValidID_whenGatewayThrowsException_thenShouldReturnException() {
        final var aCategory = Category.newCategoryWith("Film", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        doThrow(new IllegalStateException("Gateway error"))
            .when(categoryGateway).deleteById(eq(expectedId));

        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }
}
