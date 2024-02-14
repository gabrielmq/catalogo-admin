package com.fullcycle.catalogo.admin.infrastructure.configuration.usecases;

import com.fullcycle.catalogo.admin.application.category.create.CreateCategoryUseCase;
import com.fullcycle.catalogo.admin.application.category.create.DefaultCreateCategoryUseCase;
import com.fullcycle.catalogo.admin.application.category.delete.DefaultDeleteCategoryUseCase;
import com.fullcycle.catalogo.admin.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.catalogo.admin.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.fullcycle.catalogo.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.catalogo.admin.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.fullcycle.catalogo.admin.application.category.retrieve.list.ListCategoriesUseCase;
import com.fullcycle.catalogo.admin.application.category.update.DefaultUpdateCategoryUseCase;
import com.fullcycle.catalogo.admin.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.catalogo.admin.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class CategoryUseCaseConfiguration {

    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfiguration(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase() {
        return new DefaultListCategoriesUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }
}
