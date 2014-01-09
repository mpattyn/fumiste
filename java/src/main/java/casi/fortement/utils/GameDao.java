package casi.fortement.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import casi.fortement.pojo.JeuSteam;

public class GameDao {

	@Autowired
	private DataSource dataSource;

	public void insert(JeuSteam jeuSteam) {

		String sql = "INSERT INTO users "
				+ "(id_user, game_id, playtime) VALUES (?, ?, ?)";
		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(2, jeuSteam.getGameId());
			ps.setInt(3, jeuSteam.getPlaytimeForever());
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public List<JeuSteam> recupererJeux(String user_id) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Collection jeux = jdbcTemplate.query(
				"select first_name, surname from t_actor", new RowMapper() {

					public Object mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Actor actor = new Actor();
						actor.setFirstName(rs.getString("first_name"));
						actor.setSurname(rs.getString("surname"));
						return actor;
					}
				});
		return jeux;
	}

}
