package com.jros.springboot.webflux.apirest.app.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.jros.springboot.webflux.apirest.app.models.documents.Categoria;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria, String>{
	
	

}
