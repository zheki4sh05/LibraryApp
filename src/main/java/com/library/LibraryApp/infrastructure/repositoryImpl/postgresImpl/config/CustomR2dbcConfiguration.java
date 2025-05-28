package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.config;

import com.library.LibraryApp.application.dto.BookState;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.util.BookStateReadingConverter;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.util.BookStateWritingConverter;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.codec.EnumCodec;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;

import java.util.List;

@Configuration
public class CustomR2dbcConfiguration extends AbstractR2dbcConfiguration {

    @Override
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get("r2dbc:postgresql://localhost:5432/postgres");
    }

    @Override
    protected List<Object> getCustomConverters() {
        return List.of(new BookStateReadingConverter(), new BookStateWritingConverter());
    }

    @Bean
    public PostgresqlConnectionConfiguration configure(){
        return PostgresqlConnectionConfiguration.builder()
                .addHost("localhost", 5432)
                .username("postgres")
                .password("postgres")
                .codecRegistrar(EnumCodec.builder().withEnum("book_enum", BookState.class).build()).build();
    }

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, new BookStateReadingConverter(), new BookStateWritingConverter());
    }



}
