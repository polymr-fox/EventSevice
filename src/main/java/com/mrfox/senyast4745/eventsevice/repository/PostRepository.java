package com.mrfox.senyast4745.eventsevice.repository;

import com.mrfox.senyast4745.eventsevice.model.PostModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<PostModel, Long> {
    Iterable<PostModel> findAllByTypeAndParentIdOrderByDate(int type, Long parentId);
}
