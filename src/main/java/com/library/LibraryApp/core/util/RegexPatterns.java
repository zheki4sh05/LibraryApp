package com.library.LibraryApp.core.util;

public interface RegexPatterns {
    String UDK = "^\\d+(\\.\\d+)*(-\\d+)?$";
    String ISBN= "^(?:\\d{9}[Xx]|\\d{13})$";
}
