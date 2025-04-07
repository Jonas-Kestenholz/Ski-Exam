package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final PopulatorRoutes populator = new PopulatorRoutes();
    private final SkiLessonRoutes skiLessonRoutes = new SkiLessonRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/populate", populator.getRoutes());
            path("/skilessons", skiLessonRoutes.getRoutes());
        };
    }
}
