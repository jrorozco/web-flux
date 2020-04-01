package edu.ceti.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.ceti.app.models.entity.Alumno;

@Repository
public class IAlumosImpl implements IAlumnos {
	
	private Logger LOG =LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	@Override
	public List<Alumno> getAll() {
//		List<Alumno> alumnos= new ArrayList<Alumno>();
//		alumnos=em.createQuery("Select a From Alumno a").getResultList();
//		alumnos.stream().forEach(a -> LOG.info("[ALUMNO] -> {}" , a));
		return em.createQuery("Select a From Alumno a").getResultList();
	}

	@Override
	public Alumno findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void saveAlumno(Alumno alumno) {
		 em.persist(alumno);
	}

	@Override
	public void deleteAlumno(Long id) {
		// TODO Auto-generated method stub

	}

}
