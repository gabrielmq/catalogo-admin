package com.fullcycle.catalogo.admin.application;

import com.fullcycle.catalogo.admin.domain.category.Category;

public class UseCase {

    public Category execute() {
        return new Category();
    }
}