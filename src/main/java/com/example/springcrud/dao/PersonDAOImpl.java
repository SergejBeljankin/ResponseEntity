package com.example.springcrud.dao;

import com.example.springcrud.entities.Person;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@Repository
public class PersonDAOImpl implements PersonDAO {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Person> getAll(){
        return entityManager.createQuery("from Person", Person.class).getResultList();
    }


    @Override
    public Person select(Long id){
        return entityManager
                .createQuery("select person from Person as person join fetch person.roles where person.id =:id", Person.class)
                .setParameter("id", id)
                .getSingleResult();

    }

    @Override
    public void save(Person person){
        entityManager.persist(person);
    }

    @Override
    public void delete(Long id){
        entityManager.remove(select(id));
    }

    @Override
    public void update(Long id, Person personVariable){
        Person person = select(id);
        person.setUsername(personVariable.getUsername());
        person.setPassword(personVariable.getPassword());//
        person.setSurname(personVariable.getSurname());
        person.setName(personVariable.getName());
        person.setRoles(personVariable.getRoles());
        entityManager.merge(person);
    }



    @Override
    @Transactional
    public List<Person> findPersonByRole(String roleName) {
        return entityManager.createQuery("select person from Person person join fetch Role role on person.id = role.id where role.name = :roleName", Person.class)
                .setParameter("roleName", roleName).getResultList();
    }


    @Transactional
    public Person findByUserName(String username){
        return entityManager.createQuery("select person from Person as person join fetch person.roles where person.username =:username", Person.class)
                .setParameter("username", username).getSingleResult();
    }
}
