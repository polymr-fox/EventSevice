package com.mrfox.senyast4745.eventsevice.repository;

import com.mrfox.senyast4745.eventsevice.model.EventModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepository extends CrudRepository<EventModel, Long> {
    Iterable<EventModel> findAllBySubsCount(int subsCount);
    Iterable<EventModel> findAllByCreatorId(Long creatorId);
}
