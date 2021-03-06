package casi.fortement.fumiste;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import casi.fortement.pojo.JeuSteam;

@Controller
public class ResultatsController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@RequestMapping("resultats/{id}")
	public ModelAndView aficherResulats(@PathVariable String id) {
		ModelAndView result = new ModelAndView("resultats");

		List<JeuSteam> jeux = recupererJeux(id);

		long start = System.currentTimeMillis();
		result.addObject("jeux", getGamesNames(jeux));
		long end = System.currentTimeMillis() - start;
		System.out.println("Indicateur 3 : " + String.valueOf(end));
		
		start = System.currentTimeMillis();
		result.addObject("recommandations", getRecommandations(id));
		end = System.currentTimeMillis() - start;
		System.out.println("Indicateur 1 : " + String.valueOf(end));
		
		return result;
	}

	private List<JeuSteam> recupererJeux(String user_id) {
		Collection jeux = this.jdbcTemplate.query(
				"select id_game, playtime from users where id_user=" + user_id,
				new RowMapper() {
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

	private List<String> getRecommandations(String userId) {
		Collection jeux = this.jdbcTemplate.query(
				"select * from users where id_user <>" + userId,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						JeuSteam jeu = new JeuSteam();
						jeu.setProprioId("id_user");
						jeu.setGameId(rs.getInt("id_game"));
						jeu.setPlaytimeForever(rs.getInt("playtime"));
						return jeu;
					}
				});
		// Recuperation de la liste de jeu de chaque joueur
		Map<String, List<JeuSteam>> tmp = new HashMap<String, List<JeuSteam>>();
		HashMap<String, Integer> compteurJeux = new HashMap<String, Integer>();

		for (Object jeu : jeux) {
			JeuSteam jeuTmp = (JeuSteam) jeu;
			List<JeuSteam> listTmp = new ArrayList<JeuSteam>();
			if (tmp.containsKey(jeuTmp.getProprioId()))
				listTmp = tmp.get(jeuTmp.getProprioId());
			listTmp.add(jeuTmp);
			tmp.put(jeuTmp.getProprioId(), listTmp);
			compteurJeux.put(String.valueOf(jeuTmp.getGameId()), 0);
		}

		// on lance le calcul
		for (List<JeuSteam> collectionUser : tmp.values()) {
			List<JeuSteam> jeuxPreferesTmp = new ArrayList<JeuSteam>();
			int compteur = 0;
			List<Integer> tempsTmp = new ArrayList<Integer>();
			for (JeuSteam jeu : collectionUser) {
				if (compteur < 3) {
					jeuxPreferesTmp.add(jeu);
					tempsTmp.add(jeu.getPlaytimeForever());
					compteur++;
				} else {
					if (jeu.getPlaytimeForever() > Collections.min(tempsTmp)) {
						jeuxPreferesTmp.remove(tempsTmp
								.lastIndexOf(Collections.min(tempsTmp)));
						tempsTmp.remove(tempsTmp.lastIndexOf(Collections
								.min(tempsTmp)));
						jeuxPreferesTmp.add(jeu);
						tempsTmp.add(jeu.getPlaytimeForever());
					}
				}
			}
			for (JeuSteam jeu : jeuxPreferesTmp) {
				int idTmp = jeu.getGameId();
				compteurJeux.put(String.valueOf(idTmp),
						compteurJeux.get(String.valueOf(idTmp)) + 1);
			}

		}
		compteurJeux = sortHashMapByValuesD(compteurJeux);

		List<String> result = new ArrayList<String>();
		Iterator<?> it = compteurJeux.entrySet().iterator();
		int compteur = 0;
		while (it.hasNext() && compteur < 3) {
			Map.Entry pairs = (Map.Entry) it.next();
			result.add(getGameName((String) pairs.getKey()));
			compteur++;
			it.remove(); // avoids a ConcurrentModificationException
		}
		return result;
	}

	private List<String> getGamesNames(List<JeuSteam> gamesId) {
		List<String> nomsJeux = new ArrayList<String>();
		System.out.println("On chope les noms des jeux, veuillez patienter.");
		for (JeuSteam jeu : gamesId) {
			nomsJeux.add(getGameName(String.valueOf(jeu.getGameId()), false));
		}
		return nomsJeux;
	}

	private String getGameName(String gameId, boolean hasProxy) {
		String result = "";
		try {
			if(hasProxy){
				System.setProperty("http.proxyHost", "cachemad.insa-rouen.fr");
				System.setProperty("http.proxyPort", "3128");
			}
			Document doc = Jsoup.connect(
					"http://store.steampowered.com/app/" + gameId).get();
			result = (doc.title().replace("on Steam", ""));
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private LinkedHashMap<String, Integer> sortHashMapByValuesD(
			HashMap<String, Integer> passedMap) {
		List<String> mapKeys = new ArrayList<String>(passedMap.keySet());
		List<Integer> mapValues = new ArrayList<Integer>(passedMap.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);

		LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();

		Iterator<Integer> valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator<String> keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				String comp1 = passedMap.get(key).toString();
				String comp2 = val.toString();

				if (comp1.equals(comp2)) {
					passedMap.remove(key);
					mapKeys.remove(key);
					sortedMap.put((String) key, (Integer) val);
					break;
				}

			}

		}
		return sortedMap;
	}
}
