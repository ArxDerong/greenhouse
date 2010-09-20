package com.springsource.greenhouse.connect;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcAccountProviderRepository implements AccountProviderRepository {
	
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JdbcAccountProviderRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public AccountProvider findAccountProviderByName(String name) {
		return jdbcTemplate.queryForObject(SELECT_ACCOUNT_PROVIDER_BY_NAME, accountProviderMapper, name);
	}

	private final RowMapper<AccountProvider> accountProviderMapper = new RowMapper<AccountProvider>() {
		public AccountProvider mapRow(ResultSet rs, int row) throws SQLException {
			String name = rs.getString("name");
			if ("facebook".equals(name)) {
				return new JdbcFacebookAccountProvider(name, rs.getString("apiKey"),
					rs.getString("secret"), rs.getString("requestTokenUrl"), 
					rs.getString("authorizeUrl"), rs.getString("accessTokenUrl"), jdbcTemplate);					
			} else if ("twitter".equals(name)) {
				return new JdbcTwitterAccountProvider(name, rs.getString("apiKey"),
					rs.getString("secret"), rs.getString("requestTokenUrl"), 
					rs.getString("authorizeUrl"), rs.getString("accessTokenUrl"), jdbcTemplate);					
			} else {
				return new JdbcAccountProvider(name, rs.getString("apiKey"), rs.getString("secret"),
					rs.getString("requestTokenUrl"), rs.getString("authorizeUrl"),
					rs.getString("accessTokenUrl"), jdbcTemplate);
			}
		}
	};
	
	private static final String SELECT_ACCOUNT_PROVIDER_BY_NAME = 
		 "select name, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl from AccountProvider where name = ?";

}