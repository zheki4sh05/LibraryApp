package com.library.LibraryApp.application.dto;

public enum BookState {
    FREE(1),
    BORROW(0);

    final Integer type;

    BookState(Integer number) {
        this.type = number;
    }

    public Integer value(){
        return type;
    }


}
