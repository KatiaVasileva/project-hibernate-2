package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.Actor;

public class ActorRepository extends BaseRepository<Actor> {

    public ActorRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Actor.class);
    }
}
