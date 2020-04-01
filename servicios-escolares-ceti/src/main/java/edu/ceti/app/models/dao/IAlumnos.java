package edu.ceti.app.models.dao;

import java.util.List;

import edu.ceti.app.models.entity.Alumno;

public interface IAlumnos {
	
	public List<Alumno> getAll();
	public Alumno findById(Long id);
	public void saveAlumno(Alumno alumno);
	public void deleteAlumno(Long id);

}
