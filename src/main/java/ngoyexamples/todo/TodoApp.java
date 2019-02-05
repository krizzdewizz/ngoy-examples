package ngoyexamples.todo;

import static ngoy.core.Util.isSet;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ngoy.Ngoy;
import ngoy.core.Component;
import ngoy.core.Inject;
import ngoy.core.LocaleProvider;
import ngoy.core.NgModule;
import ngoy.core.OnDestroy;
import ngoy.core.Provider;
import ngoy.forms.FormsModule;
import ngoyexamples.BeanInjector;
import ngoyexamples.Example;
import ngoyexamples.todo.components.TodoComponent;
import ngoyexamples.todo.services.TodoService;

@Component(selector = "", templateUrl = "app.component.html", styleUrls = { "app.component.css" })
@NgModule(imports = { FormsModule.class }, declarations = { TodoComponent.class })
@Controller
@RequestMapping("/todo/**")
@Example("Todo")
public class TodoApp implements OnDestroy, InitializingBean {
	private static final String REDIRECT_HOME = "redirect:/todo";

	@Autowired
	private BeanInjector beanInjector;

	private Ngoy<TodoApp> ngoy;

	@Inject
	public TodoService todoService;

	public String deletedTodo;
	public boolean textRequired;

	@Override
	public void onDestroy() {
		deletedTodo = null;
		textRequired = false;
	}

	public List<Todo> getTodos() {
		return todoService.getTodos();
	}

	@PostMapping("add")
	public String addTodo(String text) throws Exception {
		boolean ok = isSet(text);
		textRequired = !ok;
		if (ok) {
			todoService.addTodo(text);
		}
		return REDIRECT_HOME;
	}

	@PostMapping("delete")
	public String deleteTodo(String id) throws Exception {
		todoService.getTodo(id)
				.ifPresent(todo -> {
					deletedTodo = todo.text;
					todoService.deleteTodo(id);
				});
		return REDIRECT_HOME;
	}

	private void createApp() {
		ngoy = Ngoy.app(TodoApp.class)
				.injectors(beanInjector)
				.translateBundle("ngoyexamples/todo/messages")
				.providers(Provider.useValue(LocaleProvider.class, LocaleContextHolder::getLocale))
				.build();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		createApp();
	}

	@GetMapping()
	public void home(HttpServletResponse response) throws Exception {
		// re-create while developing to have changes picked-up
		createApp();
		ngoy.render(response.getOutputStream());
	}
}
