package casi.fortement.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import casi.fortement.pojo.JeuSteam;

@Component
    public class GameDao {

    @Autowired
	private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("unchecked")
	public List<JeuSteam> recupererJeux(String user_id) {
	Collection jeux = this.jdbcTemplate.query(
						  "select id_game, from t_actor", new RowMapper() {
							  public Object mapRow(ResultSet rs, int rowNum)
							      throws SQLException {
							      JeuSteam jeu = new JeuSteam();
							      jeu.setGameId(rs.getInt("id_game"));
							      jeu.setPlaytimeForever(rs.getInt("playtime"));
							      return jeu;
							  }
						      });
	List<JeuSteam> resultats = new ArrayList<JeuSteam>();
	for (Object jeu : jeux) {
	    resultats.add((JeuSteam) jeu);
	}
	return resultats;
    }

}
