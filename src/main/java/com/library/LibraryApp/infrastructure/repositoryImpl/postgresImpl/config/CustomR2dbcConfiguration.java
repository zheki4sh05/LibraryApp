package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.config;

import com.library.LibraryApp.application.dto.BookState;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.util.BookStateReadingConverter;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.util.BookStateWritingConverter;
import io.r2dbc.postgresql.*;
import io.r2dbc.postgresql.codec.EnumCodec;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.*;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.*;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.mapping.*;
import org.springframework.r2dbc.core.*;

import java.util.*;

@Configuration
public class CustomR2dbcConfiguration extends AbstractR2dbcConfiguration {

    @Value("${spring.r2dbc.username}")
    private String username;

    @Value("${spring.r2dbc.password}")
    private String password;

    @Value("${spring.database.port}")
    private Integer dbPort;

    @Value("${spring.database.hostname}")
    private String hostName;

    @Value("${spring.database.name}")
    private String database;

    @Value("${spring.database.enumName}")
    private String bookEnumTypeName;

    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(hostName)
                        .port(dbPort)
                        .username(username)
                        .password(password)
                        .database(database)
                        .codecRegistrar(EnumCodec.builder().withEnum(bookEnumTypeName, BookState.class).build())
                        .build()
        );
    }

    @Override
    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new BookStateReadingConverter());
        converters.add(new BookStateWritingConverter());

        return new R2dbcCustomConversions(
                R2dbcCustomConversions.StoreConversions.of(
                        PostgresDialect.INSTANCE.getSimpleTypeHolder(),
                        getCustomConverters()
                ),
                converters
        );
    }

    @Override
    protected List<Object> getCustomConverters() {
        return List.of(
                new BookStateReadingConverter(),
                new BookStateWritingConverter()
        );
    }





}
