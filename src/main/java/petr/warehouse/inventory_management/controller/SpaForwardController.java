package petr.warehouse.inventory_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//Отдаёт index.html на клиентских маршрутах SPA.
//Список путей явный, чтобы не перехватывать API, Swagger и статику.
@Controller
public class SpaForwardController {
    @RequestMapping({"/", "/login", "/register", "/storages/**"})
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}
