package dat.security.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.security.controllers.SecurityController;
import dat.security.enums.Role;
import dat.utils.Utils;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SecurityRoutes {
    private static final ObjectMapper jsonMapper = new Utils().getObjectMapper();
    private static final SecurityController securityController = SecurityController.getInstance();

    public static EndpointGroup getSecurityRoutes() {
        return () -> {
            get("/healthcheck", securityController::healthCheck, Role.ANYONE);
            post("/login", securityController.login(), Role.ANYONE);
            post("/register", securityController.register(), Role.ANYONE);
            post("/user/addrole", securityController.addRole(), Role.ADMIN);
        };
    }

    public static EndpointGroup getSecuredRoutes() {
        return () -> {
            path("/protected", () -> {
                get("/user_demo", ctx ->
                                ctx.json(jsonMapper.createObjectNode().put("msg", "Hello from USER Protected")),
                        Role.USER);

                get("/admin_demo", ctx ->
                                ctx.json(jsonMapper.createObjectNode().put("msg", "Hello from ADMIN Protected")),
                        Role.ADMIN);
            });
        };
    }
}
