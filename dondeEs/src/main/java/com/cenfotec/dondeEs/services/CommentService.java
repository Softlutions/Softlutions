package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.repositories.CommentRepository;

@Service
public class CommentService implements CommentServiceInterface {
	@Autowired private CommentRepository commentRepository;
}