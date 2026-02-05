package com.banking.solution.utils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClientIdGenerator {

    private final JdbcTemplate jdbc;

    public ClientIdGenerator(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public String generate() {
        Long seq = jdbc.queryForObject("SELECT nextval('clientid_seq')", Long.class);
        return String.format("CLI-%06d", seq);
    }
}
