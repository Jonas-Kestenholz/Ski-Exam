package dat.daos.impl;

import dat.daos.IDAO;
import dat.daos.ISkiLessonInstructorDAO;
import dat.dtos.SkiLessonDTO;
import dat.entities.Instructor;
import dat.entities.SkiLesson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SkiLessonDAO implements IDAO<SkiLessonDTO, Integer>, ISkiLessonInstructorDAO {

    private final EntityManagerFactory emf;

    public SkiLessonDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public SkiLessonDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            SkiLesson lesson = em.find(SkiLesson.class, id);
            return lesson != null ? new SkiLessonDTO(lesson) : null;
        }
    }

    @Override
    public List<SkiLessonDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT s FROM SkiLesson s", SkiLesson.class)
                    .getResultList()
                    .stream()
                    .map(SkiLessonDTO::new)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public SkiLessonDTO create(SkiLessonDTO dto) {
        SkiLesson lesson = SkiLesson.builder()
                .name(dto.getName())
                .starttime(dto.getStarttime())
                .endtime(dto.getEndtime())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .price(dto.getPrice())
                .level(dto.getLevel())
                .build();

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(lesson);
            em.getTransaction().commit();
        }

        return new SkiLessonDTO(lesson);
    }

    @Override
    public SkiLessonDTO update(Integer id, SkiLessonDTO dto) {
        try (EntityManager em = emf.createEntityManager()) {
            SkiLesson lesson = em.find(SkiLesson.class, id);
            if (lesson == null) return null;

            em.getTransaction().begin();
            lesson.setName(dto.getName());
            lesson.setStarttime(dto.getStarttime());
            lesson.setEndtime(dto.getEndtime());
            lesson.setLatitude(dto.getLatitude());
            lesson.setLongitude(dto.getLongitude());
            lesson.setPrice(dto.getPrice());
            lesson.setLevel(dto.getLevel());
            em.getTransaction().commit();

            return new SkiLessonDTO(lesson);
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            SkiLesson lesson = em.find(SkiLesson.class, id);
            if (lesson == null) return;

            em.getTransaction().begin();
            em.remove(lesson);
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(SkiLesson.class, id) != null;
        }
    }

    @Override
    public void addInstructorToSkiLesson(int lessonId, int instructorId) {
        try (EntityManager em = emf.createEntityManager()) {
            SkiLesson lesson = em.find(SkiLesson.class, lessonId);
            Instructor instructor = em.find(Instructor.class, instructorId);
            if (lesson == null || instructor == null) return;

            em.getTransaction().begin();
            lesson.setInstructor(instructor);
            em.getTransaction().commit();
        }
    }

    @Override
    public Set<SkiLessonDTO> getSkiLessonsByInstructor(int instructorId) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT s FROM SkiLesson s WHERE s.instructor.id = :id", SkiLesson.class)
                    .setParameter("id", instructorId)
                    .getResultList()
                    .stream()
                    .map(SkiLessonDTO::new)
                    .collect(Collectors.toSet());
        }
    }
}
