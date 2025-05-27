package com.library.LibraryApp.application.dto;

public record SearchBookDto(

        String udk,
        String name,
        String author

) {
    public SearchBookDto(){
        this("", "", "");
    }



}
