package casi.fortement.fumiste;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResultatsController {

	@RequestMapping("/resultats/{id}")
	public ModelAndView aficherResulats(@PathVariable String id) {
		ModelAndView result = new ModelAndView("resultats.jsp");
		
		return null;
	}
}
