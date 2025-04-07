package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.InstructorDAO;
import dat.daos.impl.SkiLessonDAO;
import dat.dtos.SkiLessonDTO;
import dat.security.enums.Role;
import dat.security.exceptions.ApiException;
import dk.bugelhartmann.UserDTO;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dat.utils.ExternalSkiApiFetcher;

public class SkiLessonController implements IController<SkiLessonDTO, Integer> {

    private final SkiLessonDAO skiLessonDAO;
    private final InstructorDAO instructorDAO;

    public SkiLessonController() {
        this.skiLessonDAO = new SkiLessonDAO(HibernateConfig.getEntityManagerFactory());
        this.instructorDAO = new InstructorDAO(HibernateConfig.getEntityManagerFactory());
    }

    private boolean isAdmin(UserDTO user) {
        return user != null && user.getRoles().contains(Role.ADMIN.name());
    }

    @Override
    public void read(Context ctx) throws ApiException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        var dto = skiLessonDAO.read(id);

        if (dto == null) {
            throw new ApiException(404, "Ski lesson with id " + id + " not found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("skiLesson", dto);

        try {
            var instructions = ExternalSkiApiFetcher.getInstructionsByLevel(dto.getLevel().name());
            response.put("instructions", instructions);
        } catch (Exception e) {
            response.put("instructions", List.of());
            response.put("warning", "Instructions could not be fetched");
        }

        ctx.json(response);
    }

    @Override
    public void readAll(Context ctx) {
        List<SkiLessonDTO> all = skiLessonDAO.readAll();
        ctx.json(all);
    }

    @Override
    public void create(Context ctx) {
        try {
            SkiLessonDTO dto = ctx.bodyAsClass(SkiLessonDTO.class);
            System.out.println("📦 MODTAGER DTO: " + dto);

            SkiLessonDTO created = skiLessonDAO.create(dto);
            ctx.status(201).json(created);
        } catch (Exception e) {
            e.printStackTrace(); // Print fuld fejl i konsollen
            throw new ApiException(500, "Create failed: " + e.getMessage());
        }
    }

    @Override
    public void update(Context ctx) throws ApiException {
        UserDTO user = ctx.attribute("user");
        if (!isAdmin(user)) {
            throw new ApiException(401, "Only ADMINs can update ski lessons");
        }

        int id = Integer.parseInt(ctx.pathParam("id"));
        SkiLessonDTO dto = ctx.bodyAsClass(SkiLessonDTO.class);
        SkiLessonDTO updated = skiLessonDAO.update(id, dto);

        if (updated == null) {
            throw new ApiException(404, "Ski lesson not found");
        }

        ctx.json(updated);
    }

    @Override
    public void delete(Context ctx) throws ApiException {
        UserDTO user = ctx.attribute("user");
        if (!isAdmin(user)) {
            throw new ApiException(401, "Only ADMINs can delete ski lessons");
        }

        int id = Integer.parseInt(ctx.pathParam("id"));
        if (!skiLessonDAO.validatePrimaryKey(id)) {
            throw new ApiException(404, "Ski lesson not found");
        }

        skiLessonDAO.delete(id);
        ctx.status(204);
    }

    public void filterByLevel(Context ctx) {
        String level = ctx.queryParam("level");
        if (level == null) {
            ctx.status(400).json(Map.of("error", "Missing 'level' query param"));
            return;
        }

        List<SkiLessonDTO> filtered = skiLessonDAO.readAll().stream()
                .filter(dto -> dto.getLevel().name().equalsIgnoreCase(level))
                .collect(Collectors.toList());

        ctx.json(filtered);
    }

    public void addInstructor(Context ctx) throws ApiException {
        UserDTO user = ctx.attribute("user");
        if (!isAdmin(user)) {
            throw new ApiException(401, "Only ADMINs can assign instructors");
        }

        int lessonId = Integer.parseInt(ctx.pathParam("lessonId"));
        int instructorId = Integer.parseInt(ctx.pathParam("instructorId"));
        skiLessonDAO.addInstructorToSkiLesson(lessonId, instructorId);
        ctx.status(204);
    }

    public void getByInstructor(Context ctx) {
        int instructorId = Integer.parseInt(ctx.pathParam("id"));
        ctx.json(skiLessonDAO.getSkiLessonsByInstructor(instructorId));
    }

    public void totalPricePerInstructor(Context ctx) {
        Map<Integer, Double> result = skiLessonDAO.readAll().stream()
                .filter(dto -> dto.getInstructor() != null)
                .collect(Collectors.groupingBy(
                        dto -> dto.getInstructor().getId(),
                        Collectors.summingDouble(SkiLessonDTO::getPrice)
                ));

        ctx.json(result);
    }

    public void getInstructionDuration(Context ctx) throws ApiException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        var dto = skiLessonDAO.read(id);
        if (dto == null) {
            throw new ApiException(404, "Ski lesson not found");
        }

        int totalDuration = ExternalSkiApiFetcher.getInstructionsByLevel(dto.getLevel().name())
                .stream()
                .mapToInt(ins -> ins.getDurationMinutes())
                .sum();

        ctx.json(Map.of("lessonId", id, "totalDuration", totalDuration));
    }
}
