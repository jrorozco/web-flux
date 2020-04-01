package mc.com.jros.microservicios.app.usuarios.service;

import org.springframework.stereotype.Service;

import mc.com.jros.microservicios.app.usuarios.models.repository.AlumnoRepository;
import mc.com.jros.microservicios.commons.alumnos.models.entity.Alumno;
import mc.com.jros.microservicios.commons.services.CommonServiceImpl;

@Service
public class AlumnoServiceImpl extends CommonServiceImpl<Alumno, AlumnoRepository> implements AlumnoService{
	

}
