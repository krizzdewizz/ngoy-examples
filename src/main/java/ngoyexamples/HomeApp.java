package ngoyexamples;

import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ngoy.Ngoy;
import ngoy.core.Component;
import ngoy.core.OnInit;

@Controller
@RequestMapping("/")
@Component(selector = "", template = "<h1>ngoy examples</h1><a *ngFor=\"let it of examples\" [href]=\"it.href\">{{it.name}}</a>")
public class HomeApp implements InitializingBean, OnInit {

	public static class Example {
		public final String href;
		public final String name;

		public Example(String href, String name) {
			this.href = href;
			this.name = name;
		}
	}

	@Autowired
	private BeanInjector beanInjector;

	public Example[] examples;

	@Override
	public void ngOnInit() {
		ApplicationContext context = beanInjector.context;
		String[] exampleApps = context.getBeanNamesForAnnotation(ExampleName.class);
		examples = Stream.of(exampleApps)
				.map(ex -> {
					Class<?> appClass = context.getBean(ex)
							.getClass();

					String requestMapping = appClass.getAnnotation(RequestMapping.class)
							.value()[0];

					return new Example(requestMapping.replace("*", "")
							.replace("//", "/"),
							appClass.getAnnotation(ExampleName.class)
									.value());
				})
				.toArray(Example[]::new);
	}

	private Ngoy<HomeApp> app;

	@GetMapping()
	public void home(HttpServletResponse response) throws Exception {
		app.render(response.getOutputStream());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		app = Ngoy.app(HomeApp.class)
				.injectors(beanInjector)
				.build();
	}
}
