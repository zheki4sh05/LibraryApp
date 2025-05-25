package com.library.LibraryApp.core.util;

public interface RegexPatterns {
    String UDK = "^\\d+(\\.\\d+)*(-\\d+)?$";
    String ISBN= "^(?:\\d{9}[Xx]|\\d{13})$";
    String UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
}
