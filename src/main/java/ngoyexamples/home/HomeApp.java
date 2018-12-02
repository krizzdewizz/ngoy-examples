package ngoyexamples.home;

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
import ngoyexamples.BeanInjector;
import ngoyexamples.Example;

@Controller
@RequestMapping("/")
@Component(selector = "", templateUrl = "app.component.html")
public class HomeApp implements InitializingBean, OnInit {

	public static class ExampleItem {
		public final String href;
		public final String name;

		public ExampleItem(String href, String name) {
			this.href = href;
			this.name = name;
		}
	}

	@Autowired
	private BeanInjector beanInjector;

	public ExampleItem[] examples;

	@Override
	public void ngOnInit() {
		ApplicationContext context = beanInjector.context;
		String[] exampleApps = context.getBeanNamesForAnnotation(Example.class);
		examples = Stream.of(exampleApps)
				.sorted()
				.map(ex -> {
					Class<?> appClass = context.getBean(ex)
							.getClass();

					String requestMapping = appClass.getAnnotation(RequestMapping.class)
							.value()[0];

					return new ExampleItem(requestMapping.replace("*", "")
							.replace("//", "/"),
							appClass.getAnnotation(Example.class)
									.value());
				})
				.toArray(ExampleItem[]::new);
	}

	private Ngoy<HomeApp> app;

	@GetMapping()
	public void home(HttpServletResponse response) throws Exception {
		// re-create while developing to have changes picked-up
//		createApp();
		app.render(response.getOutputStream());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		createApp();
	}

	private void createApp() {
		app = Ngoy.app(HomeApp.class)
				.injectors(beanInjector)
				.build();
	}
}
