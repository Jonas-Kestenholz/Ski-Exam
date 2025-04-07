package dat.daos;

import dat.dtos.SkiLessonDTO;
import java.util.Set;

public interface ISkiLessonInstructorDAO {
    void addInstructorToSkiLesson(int lessonId, int instructorId);
    Set<SkiLessonDTO> getSkiLessonsByInstructor(int instructorId);
}