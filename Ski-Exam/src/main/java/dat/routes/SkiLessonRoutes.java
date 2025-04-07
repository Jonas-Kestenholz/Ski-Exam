package dat.routes;

import dat.config.HibernateConfig;
import dat.controllers.impl.SkiLessonController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SkiLessonRoutes {

    private final SkiLessonController controller;

    public SkiLessonRoutes() {
        this.controller = new SkiLessonController();
    }

    public EndpointGroup getRoutes() {
        return () -> {
            get("/", controller::readAll);
            get("/{id}", controller::read);
            get("/filter", controller::filterByLevel);

            post("/", controller::create);
            put("/{id}", controller::update);
            delete("/{id}", controller::delete);
            put("/{lessonId}/instructors/{instructorId}", controller::addInstructor);
            get("/instructor/{id}", controller::getByInstructor);
            get("/instructors/totalprice", controller::totalPricePerInstructor);
            get("/{id}/instructions/totalduration", controller::getInstructionDuration);
        };
    }
}
