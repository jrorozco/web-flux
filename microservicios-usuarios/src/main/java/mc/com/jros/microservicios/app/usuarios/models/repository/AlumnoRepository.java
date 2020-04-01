package mc.com.jros.microservicios.app.usuarios.models.repository;

import org.springframework.data.repository.CrudRepository;

import mc.com.jros.microservicios.commons.alumnos.models.entity.Alumno;

public interface AlumnoRepository extends CrudRepository<Alumno, Long> {

}
