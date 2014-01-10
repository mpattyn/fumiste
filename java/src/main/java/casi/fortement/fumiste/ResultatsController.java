package casi.fortement.fumiste;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import casi.fortement.pojo.JeuSteam;
import casi.fortement.utils.GameDao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Controller
    public class ResultatsController {

    @Autowired
	private JdbcTemplate jdbcTemplate;

    @RequestMapping("resultats/{id}")
	public ModelAndView aficherResulats(@PathVariable String id) {
	ModelAndView result = new ModelAndView("resultats");

	List<JeuSteam> jeux = recupererJeux(id);
	result.addObject("jeux", jeux);
		
	return result;
    }

    private List<JeuSteam> recupererJeux(String user_id) {
	Collection jeux = this.jdbcTemplate.query(
						  "select id_game, playtime from users", new RowMapper() {
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
