package mc.com.jros.microservicios.app.cursos.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import mc.com.jros.microservicios.app.cursos.models.entity.Curso;
import mc.com.jros.microservicios.app.cursos.services.CursoService;
import mc.com.jros.microservicios.commons.alumnos.models.entity.Alumno;
import mc.com.jros.microservicios.controllers.CommonController;

@RestController
public class CursoController extends CommonController<Curso, CursoService>{

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@RequestBody Curso curso, @PathVariable Long id)
	{
		Optional<Curso> optionalCurso= this.service.findById(id);
		if(!optionalCurso.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso cursoDB=  optionalCurso.get();
		cursoDB.setNombre(curso.getNombre());
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(cursoDB));
	}
	
	@PutMapping("/{id}/asignar-alumnos")
	public ResponseEntity<?> asignarAlumnos(@RequestBody List<Alumno> alumnos, @PathVariable Long id){
		Optional<Curso> optionalCurso= this.service.findById(id);
		if(!optionalCurso.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso cursoDB=  optionalCurso.get();
		alumnos.forEach(a -> {cursoDB.addAlumnos(a);});
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(cursoDB));
	}
	
	@PutMapping("/{id}/eliminar-alumno")
	public ResponseEntity<?> asignarAlumnos(@RequestBody Alumno alumno, @PathVariable Long id){
		Optional<Curso> optionalCurso= this.service.findById(id);
		if(!optionalCurso.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso cursoDB=  optionalCurso.get();
		cursoDB.removeAlumnos(alumno);
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(cursoDB));
	}
}
