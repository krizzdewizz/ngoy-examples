package ngoyexamples.hilo;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ngoy.Ngoy;
import ngoy.core.Component;
import ngoy.core.NgModule;
import ngoy.forms.FormsModule;
import ngoyexamples.BeanInjector;
import ngoyexamples.ExampleName;

@Component(selector = "", templateUrl = "app.component.html")
@NgModule(imports = { FormsModule.class })
@Controller
@RequestMapping("/hilo/**")
@ExampleName("High-Low Game")
public class HiLoApp implements InitializingBean {

	private static final String REDIRECT_URL = "redirect:/hilo";

	private Ngoy<HiLoApp> ngoy;

	@Autowired
	private BeanInjector beanInjector;

	public final String title = HiLoApp.class.getAnnotation(ExampleName.class)
			.value();

	public final Game game = new Game();

	@PostMapping("guess")
	public String guess(int number) {
		game.guess(number);
		return REDIRECT_URL;
	}

	@PostMapping("init")
	public String initGame() {
		game.init();
		return REDIRECT_URL;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ngoy = Ngoy.app(HiLoApp.class)
				.injectors(beanInjector)
				.build();
	}

	@GetMapping()
	public void home(HttpServletResponse response) throws Exception {
		ngoy.render(response.getOutputStream());
	}
}
