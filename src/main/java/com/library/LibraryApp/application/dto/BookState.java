package com.library.LibraryApp.application.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BookState {
    FREE("FREE"),
    BORROW("BORROW");

    private String value;

    BookState(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}

