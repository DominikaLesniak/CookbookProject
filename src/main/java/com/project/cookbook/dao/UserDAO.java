package com.project.cookbook.dao;

import com.project.cookbook.model.User;

import javax.persistence.EntityManager;

public class UserDAO extends EntityManagerDAO {

    public void insertUser(User user) {
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
    }
}
