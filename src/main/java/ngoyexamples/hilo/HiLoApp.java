package ngoyexamples.hilo;

import ngoy.Ngoy;
import ngoy.core.Component;
import ngoy.core.NgModule;
import ngoy.forms.FormsModule;
import ngoyexamples.BeanInjector;
import ngoyexamples.Example;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Component(selector = "", templateUrl = "hilo.component.html")
@NgModule(imports = {FormsModule.class})
@Controller
@RequestMapping("/hilo/**")
@Example("High-Low Game")
public class HiLoApp implements InitializingBean {

    private static final String REDIRECT_URL = "redirect:/hilo";

    private Ngoy<HiLoApp> ngoy;

    @Autowired
    private BeanInjector beanInjector;

    public final String title = HiLoApp.class.getAnnotation(Example.class)
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
        createApp();
    }

    private void createApp() {
        ngoy = Ngoy.app(HiLoApp.class)
                .injectors(beanInjector)
                .build();
    }

    @GetMapping()
    public void home(HttpServletResponse response) throws Exception {
        // re-create while developing to have changes picked-up
//		createApp();
        ngoy.render(response.getOutputStream());
    }
}
