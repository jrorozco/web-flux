package edu.ceti.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.ceti.app.models.dao.IAlumnos;
import edu.ceti.app.models.entity.Alumno;

@Controller
public class AlumnoController {
	
	@Autowired
	private IAlumnos alumnosDao;
	
	
	@RequestMapping(value="listar_alumnos", method=RequestMethod.GET)
	public String listarAlumno(Model model) {
		model.addAttribute("titulo","Listado de Alumnos");
		model.addAttribute("alumnos",alumnosDao.getAll());
		return "listar_alumnos";
	}

	@RequestMapping(value="/form",method=RequestMethod.GET)
	public String formCrearAlumno(Map<String, Object> model) {
		Alumno alumno = new Alumno();
		model.put("titulo", "Crear Nuevo Alumno");
		model.put("alumno", alumno);
		return "/form";
	}
	
	@RequestMapping(value="/form", method=RequestMethod.POST)
	public String guardarAlumno(Alumno alumno) {
		alumnosDao.saveAlumno(alumno);
		return "listar_alumnos";
	}
}
