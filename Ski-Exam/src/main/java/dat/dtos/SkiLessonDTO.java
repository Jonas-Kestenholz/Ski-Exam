package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.entities.SkiLesson;
import dat.entities.Level;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkiLessonDTO {
    private int id;
    private String name;
    private LocalDateTime starttime;
    private LocalDateTime endtime;
    private double latitude;
    private double longitude;
    private double price;
    private Level level;
    private InstructorDTO instructor;

    public SkiLessonDTO(SkiLesson lesson) {
        this.id = lesson.getId();
        this.name = lesson.getName();
        this.starttime = lesson.getStarttime();
        this.endtime = lesson.getEndtime();
        this.latitude = lesson.getLatitude();
        this.longitude = lesson.getLongitude();
        this.price = lesson.getPrice();
        this.level = lesson.getLevel();
        if (lesson.getInstructor() != null) {
            this.instructor = new InstructorDTO(lesson.getInstructor());
        }
    }
}
