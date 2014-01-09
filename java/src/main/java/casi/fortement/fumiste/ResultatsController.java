package casi.fortement.fumiste;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import casi.fortement.pojo.JeuSteam;
import casi.fortement.utils.GameDao;

@Controller
public class ResultatsController {

	@RequestMapping("resultats/{id}")
	public ModelAndView aficherResulats(@PathVariable String id) {
		ModelAndView result = new ModelAndView("resultats.jsp");

		List<JeuSteam> jeux = new GameDao().recupererJeux(id);
		result.addObject("jeux", jeux);
		
		return result;
	}
}
