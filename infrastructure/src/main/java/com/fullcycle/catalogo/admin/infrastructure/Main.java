package com.fullcycle.catalogo.admin.infrastructure;

import com.fullcycle.catalogo.admin.application.UseCase;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.out.println(new UseCase().execute());
    }
}