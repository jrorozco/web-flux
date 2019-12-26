package com.jros.springboot.webflux.app.models.documents;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Categoria {
	private String id;
	private String nombre;
	
	
	
	
	public Categoria() {
	}
	
	public Categoria(String nombre) {
		this.nombre = nombre;
	}
	
	@Id
	@NotEmpty
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	

}
