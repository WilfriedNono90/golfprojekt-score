package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Ubung;

//nom de la classe et le type de Lid
public interface UbungRepositorie extends JpaRepository<Ubung, Long> {
	
	public Ubung findByName(String name);
	
	public List<Ubung> findAll();
}
