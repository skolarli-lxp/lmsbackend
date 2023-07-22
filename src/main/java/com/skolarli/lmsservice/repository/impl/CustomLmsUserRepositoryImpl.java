package com.skolarli.lmsservice.repository.impl;

import com.skolarli.lmsservice.models.db.core.LmsUser;
import com.skolarli.lmsservice.repository.CustomLmsUserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Code for custom access methods of the user repo goes here.
 */
public class CustomLmsUserRepositoryImpl implements CustomLmsUserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public LmsUser findLmsUserByEmail(String email) {
        //return (LmsUser) entityManager.createQuery("SELECT u FROM LmsUser u WHERE u.email = :id")
        //        .setParameter("id", email)
        //        .getSingleResult();
        return null;
    }
}
