package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.InstructorDTO;
import dat.entities.Instructor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class InstructorDAO implements IDAO<InstructorDTO, Integer> {

    private final EntityManagerFactory emf;

    public InstructorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public InstructorDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Instructor instructor = em.find(Instructor.class, id);
            return instructor != null ? new InstructorDTO(instructor) : null;
        }
    }

    @Override
    public List<InstructorDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT i FROM Instructor i", Instructor.class)
                    .getResultList()
                    .stream()
                    .map(InstructorDTO::new)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public InstructorDTO create(InstructorDTO dto) {
        Instructor instructor = Instructor.builder()
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .yearsOfExperience(dto.getYearsOfExperience())
                .build();

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(instructor);
            em.getTransaction().commit();
        }

        return new InstructorDTO(instructor);
    }

    @Override
    public InstructorDTO update(Integer id, InstructorDTO dto) {
        try (EntityManager em = emf.createEntityManager()) {
            Instructor instructor = em.find(Instructor.class, id);
            if (instructor == null) return null;

            em.getTransaction().begin();
            instructor.setFirstname(dto.getFirstname());
            instructor.setLastname(dto.getLastname());
            instructor.setEmail(dto.getEmail());
            instructor.setPhone(dto.getPhone());
            instructor.setYearsOfExperience(dto.getYearsOfExperience());
            em.getTransaction().commit();

            return new InstructorDTO(instructor);
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Instructor instructor = em.find(Instructor.class, id);
            if (instructor == null) return;

            em.getTransaction().begin();
            em.remove(instructor);
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Instructor.class, id) != null;
        }
    }
}
