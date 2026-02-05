package com.banking.solution.utils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccountNumberGenerator {

    private final JdbcTemplate jdbc;

    public AccountNumberGenerator(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public String generate() {
        Long seq = jdbc.queryForObject("SELECT nextval('account_number_seq')", Long.class);
        return String.format("%010d", seq);
    }
}
