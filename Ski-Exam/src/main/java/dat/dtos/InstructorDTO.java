package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.entities.Instructor;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstructorDTO {
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private int yearsOfExperience;

    public InstructorDTO(Instructor instructor) {
        this.id = instructor.getId();
        this.firstname = instructor.getFirstname();
        this.lastname = instructor.getLastname();
        this.email = instructor.getEmail();
        this.phone = instructor.getPhone();
        this.yearsOfExperience = instructor.getYearsOfExperience();
    }
}
