package dat.routes;

import dat.config.HibernateConfig;
import dat.utils.Populator;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.get;

public class PopulatorRoutes {

    public EndpointGroup getRoutes() {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        return () -> {
            get("/", ctx -> {
                Populator.populateUsers(emf);
                ctx.result("Populated users only");
            });

            get("/ski", ctx -> {
                Populator.setup(emf);
                ctx.result("Populated users + ski lessons");
            });
        };
    }
}