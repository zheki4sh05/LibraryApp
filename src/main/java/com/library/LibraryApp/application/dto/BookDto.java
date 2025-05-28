package com.library.LibraryApp.application.dto;

import com.library.LibraryApp.core.util.RegexPatterns;
import com.library.LibraryApp.web.markers.AdvancedInfo;
import com.library.LibraryApp.web.markers.BasicInfo;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;


@Builder
public record BookDto(


        UUID id,


        String name,


            String udk,


        UUID author

){



}
