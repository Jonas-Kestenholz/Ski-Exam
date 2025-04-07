package dat.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkiLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private LocalDateTime starttime;
    private LocalDateTime endtime;
    private double latitude;
    private double longitude;
    private double price;

    @Enumerated(EnumType.STRING)
    private Level level;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;
}
