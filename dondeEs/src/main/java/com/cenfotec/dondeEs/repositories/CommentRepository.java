package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


import com.cenfotec.dondeEs.ejb.Comment;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
	List<Comment> findAll();
}
