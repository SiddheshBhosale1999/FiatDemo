package com.practice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.practice.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer>{
	//pagination
	
	@Query("from Contact as c where c.user.id=:userId")
	public List<Contact> findByUserId(@Param("userId") int userId);
}
