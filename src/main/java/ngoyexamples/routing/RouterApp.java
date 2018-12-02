package ngoyexamples.routing;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ngoy.Ngoy;
import ngoy.core.Component;
import ngoy.core.Inject;
import ngoy.core.NgModule;
import ngoy.core.Provider;
import ngoy.router.Location;
import ngoy.router.Route;
import ngoy.router.Router;
import ngoy.router.RouterConfig;
import ngoy.router.RouterModule;
import ngoyexamples.Example;
import ngoyexamples.routing.home.HomeComponent;
import ngoyexamples.routing.settings.SettingsComponent;

@Component(selector = "", templateUrl = "app.component.html")
@NgModule()
@Controller
@RequestMapping("/router/*")
@Example("Router")
public class RouterApp implements InitializingBean {

	public final String title = RouterApp.class.getAnnotation(Example.class)
			.value();

	@Inject
	public Router router;

	@Autowired
	private HttpServletRequest request;

	private Ngoy<RouterApp> ngoy;

	public List<Route> getRoutes() {
		return router.getRoutes();
	}

	public boolean isActiveRoute(Route route) {
		return router.isActive(route);
	}

	@GetMapping()
	public void home(HttpServletResponse response) throws Exception {
		// render all the routes to a folder in the file system
//		ngoy.renderSite(java.nio.file.Paths.get("site"));

		// re-create while developing to have changes picked-up
//		createApp();

		ngoy.render(response.getOutputStream());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		createApp();
	}

	private void createApp() {
		RouterConfig routerConfig = RouterConfig //
				.baseHref("/router")
				.location(Provider.useValue(Location.class, request::getRequestURI))
				.route("index", HomeComponent.class)
				.route("settings", SettingsComponent.class)
				.build();

		ngoy = Ngoy.app(RouterApp.class)
				.modules(RouterModule.forRoot(routerConfig))
				.build();
	}
}
