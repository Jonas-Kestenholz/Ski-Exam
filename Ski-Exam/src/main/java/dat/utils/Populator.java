package dat.utils;

import dat.config.HibernateConfig;
import dat.daos.impl.InstructorDAO;
import dat.daos.impl.SkiLessonDAO;
import dat.dtos.InstructorDTO;
import dat.dtos.SkiLessonDTO;
import dat.entities.Level;
import dat.security.entities.Role;
import dat.security.entities.User;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;

public class Populator {

    public static void setup(EntityManagerFactory emf) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Roller
            Role userRole = em.find(Role.class, "USER");
            if (userRole == null) {
                userRole = new Role("USER");
                em.persist(userRole);
            }

            Role adminRole = em.find(Role.class, "ADMIN");
            if (adminRole == null) {
                adminRole = new Role("ADMIN");
                em.persist(adminRole);
            }

            // Brugere
            User user = em.find(User.class, "usertest");
            if (user == null) {
                user = new User("usertest", "user123");
                user.addRole(userRole);
                em.persist(user);
            }

            User admin = em.find(User.class, "admintest");
            if (admin == null) {
                admin = new User("admintest", "admin123");
                admin.addRole(adminRole);
                em.persist(admin);
            }

            em.getTransaction().commit();
        }

        // Du kan også populere ski lessons bagefter, som du gør nu
        populateSkiLessons(HibernateConfig.getEntityManagerFactory());
    }

    public static UserDTO[] populateUsers(EntityManagerFactory emf) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Role userRole = em.find(Role.class, "USER");
            if (userRole == null) {
                userRole = new Role("USER");
                em.persist(userRole);
            }

            Role adminRole = em.find(Role.class, "ADMIN");
            if (adminRole == null) {
                adminRole = new Role("ADMIN");
                em.persist(adminRole);
            }

            User user = new User("usertest", "user123");
            user.addRole(userRole);

            User admin = new User("admintest", "admin123");
            admin.addRole(adminRole);

            em.persist(user);
            em.persist(admin);

            em.getTransaction().commit();

            return new UserDTO[]{
                    new UserDTO(user.getUsername(), "user123"),
                    new UserDTO(admin.getUsername(), "admin123")
            };
        }
    }

    public static void populateSkiLessons(EntityManagerFactory emf) {
        InstructorDAO instructorDAO = new InstructorDAO(emf);
        SkiLessonDAO skiLessonDAO = new SkiLessonDAO(emf);

        InstructorDTO instructor1 = instructorDAO.create(
                InstructorDTO.builder()
                        .firstname("Anna")
                        .lastname("Skisweet")
                        .email("anna@ski.com")
                        .phone("12345678")
                        .yearsOfExperience(5)
                        .build());

        InstructorDTO instructor2 = instructorDAO.create(
                InstructorDTO.builder()
                        .firstname("Lars")
                        .lastname("Powderpro")
                        .email("lars@powder.dk")
                        .phone("87654321")
                        .yearsOfExperience(10)
                        .build());

        SkiLessonDTO lesson1 = skiLessonDAO.create(
                SkiLessonDTO.builder()
                        .name("Morning Shred")
                        .starttime(LocalDateTime.of(2025, 1, 10, 9, 0))
                        .endtime(LocalDateTime.of(2025, 1, 10, 11, 0))
                        .latitude(46.8)
                        .longitude(9.8)
                        .price(499.99)
                        .level(Level.BEGINNER)
                        .build());

        SkiLessonDTO lesson2 = skiLessonDAO.create(
                SkiLessonDTO.builder()
                        .name("Advanced Steep & Deep")
                        .starttime(LocalDateTime.of(2025, 1, 11, 13, 0))
                        .endtime(LocalDateTime.of(2025, 1, 11, 15, 0))
                        .latitude(47.0)
                        .longitude(10.1)
                        .price(799.99)
                        .level(Level.ADVANCED)
                        .build());

        skiLessonDAO.addInstructorToSkiLesson(lesson1.getId(), instructor1.getId());
        skiLessonDAO.addInstructorToSkiLesson(lesson2.getId(), instructor2.getId());
    }
}
