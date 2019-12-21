package com.project.cookbook.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerDAO {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.baeldung.movie_catalog");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
