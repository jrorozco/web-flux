package mc.com.jros.microservicios.app.usuarios.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import mc.com.jros.microservicios.app.usuarios.service.AlumnoService;
import mc.com.jros.microservicios.commons.alumnos.models.entity.Alumno;
import mc.com.jros.microservicios.controllers.CommonController;

@RestController
public class AlumnoController extends CommonController<Alumno, AlumnoService>{
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@RequestBody Alumno alumno, @PathVariable Long id){
		Optional<Alumno> optionalAlumno= service.findById(id);
		
		if(!optionalAlumno.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Alumno alumnoDB= optionalAlumno.get();
		alumnoDB.setNombre(alumno.getNombre());
		alumnoDB.setApellido(alumno.getApellido());
		alumnoDB.setEmail(alumno.getEmail());
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(alumnoDB));
	}
	
	
}
