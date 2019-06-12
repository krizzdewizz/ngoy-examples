package ngoyexamples.todo.components;

import ngoy.core.Component;
import ngoy.core.Input;
import ngoyexamples.todo.Todo;
import org.springframework.stereotype.Controller;

@Component(selector = "todo", templateUrl = "todo.component.html", styleUrls = {"todo.component.css"})
@Controller
public class TodoComponent {
    @Input
    public Todo todo;
}
