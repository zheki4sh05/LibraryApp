package com.library.LibraryApp.application.dto;

public enum BookState {
    FREE("FREE"),
    BORROW("BORROW");

    private String value;

    BookState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

