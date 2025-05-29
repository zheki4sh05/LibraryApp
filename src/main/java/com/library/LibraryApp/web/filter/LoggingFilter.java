package com.library.LibraryApp.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Slf4j
public class LoggingFilter implements WebFilter {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long startTime = System.currentTimeMillis();
        log.info("Запрос получен: {} {} (время: {})", exchange.getRequest().getMethod(), exchange.getRequest().getURI(), startTime);

        return chain.filter(exchange)
                .doOnSuccess(response -> {
                    long endTime = System.currentTimeMillis();
                    log.info("Ответ отправлен: {} (время: {}) (время обработки: {} мс)",
                            exchange.getResponse().getStatusCode(), endTime, (endTime - startTime));
                });
    }
}

