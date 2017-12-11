package com.websystique.springmvc.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.websystique.springmvc.model.User;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	private static final AtomicLong counter = new AtomicLong();

	static {
		counter.incrementAndGet();
		counter.incrementAndGet();
		counter.incrementAndGet();
	}
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public List<User> findAllUsers() {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "SELECT * FROM user";
		List<User> result = namedParameterJdbcTemplate.query(sql, params, new UserMapper());
		return result;
	}

	public User findById(long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		String sql = "SELECT * FROM user WHERE id=:id";
		User result = namedParameterJdbcTemplate.queryForObject(sql, params, new UserMapper());
		return result;
	}

	public User findByName(String username) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		String sql = "SELECT * FROM user WHERE username=:username";
		User result = namedParameterJdbcTemplate.queryForObject(sql, params, new UserMapper());
		return result;
	}

	public void saveUser(User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", (int) counter.incrementAndGet());
		params.put("username", user.getUsername());
		params.put("address", user.getAddress());
		params.put("email", user.getEmail());
		namedParameterJdbcTemplate.update("insert into user values(:id, :username, :address, :email)", params);
	}

	public void updateUser(User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", (int) user.getId());
		params.put("username", user.getUsername());
		params.put("address", user.getAddress());
		params.put("email", user.getEmail());
		namedParameterJdbcTemplate
				.update("update user set username=:username, address=:address, email=:email where id=:id", params);
	}

	public void deleteUserById(long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", (int) id);
		namedParameterJdbcTemplate.update("delete from user where id=:id", params);
	}

	public boolean isUserExist(User user) {
		return findByName(user.getUsername()) != null;
	}

	public void deleteAllUsers() {
		Map<String, Object> params = new HashMap<String, Object>();
		namedParameterJdbcTemplate.update("delete from user", params);
	}

	private static final class UserMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setUsername(rs.getString("username"));
			user.setAddress(rs.getString("address"));
			user.setEmail(rs.getString("email"));
			return user;
		}
	}

}
