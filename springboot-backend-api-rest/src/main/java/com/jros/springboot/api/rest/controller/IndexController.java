package com.jros.springboot.api.rest.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jros.springboot.api.rest.models.Usuario;

@Controller // con esta anotacion la hace una clase controladora en spring...
@RequestMapping("/app")
public class IndexController {
	
	@Value("${texto.indexcontoller.titulo.index}")
	private String index_titulo;
	
	@Value("${texto.indexcontoller.titulo.perfil}")
	private String perfil_titulo;
	
	@Value("${texto.indexcontoller.titulo.listar}")
	private String listar_titulo;
	
	
//	@RequestMapping(value="/index", method=RequestMethod.GET)// forma uno
//	@GetMapping("/index") 
	@GetMapping({"/index","/","","/home"})  // si queremos que esta mapeada a mas de una url encerramos las rutas entre llaves {"/index","/","/home"}
	public String index(Model model)
	{
		model.addAttribute("titulo",index_titulo);
		return "index";
	}
	
	@RequestMapping(value="/perfil", method=RequestMethod.GET)
	public String perfil(Model model)
	{
		Usuario usuario = new Usuario();
		usuario.setNombre("Rene");
		usuario.setApellido("Orozco");
		usuario.setEmail("reneorozco@hotmail.com");
		model.addAttribute("usuario",usuario);
		model.addAttribute("titulo",perfil_titulo.concat(usuario.getNombre()));
		return "perfil";
	}

	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public String listar(Model model)
	{
//		Usuario usuario = new Usuario();
//	usuario.setNombre("Rene");
//	usuario.setApellido("Orozco");
//	usuario.setEmail("reneorozco@hotmail.com");
//	Usuario usuario2 = new Usuario();
//	usuario2.setNombre("Raul");
//	usuario2.setApellido("Gonzalez");
//	usuario2.setEmail("raulgonzalez@hotmail.com");
//	List<Usuario> usuarios = new ArrayList<>();
//	usuarios.add(usuario);
//	usuarios.add(usuario2);
//	model.addAttribute("usuarios",usuarios);
	model.addAttribute("titulo",listar_titulo);
		
		return "listar";
	}
	
	@ModelAttribute("usuarios")
	public List<Usuario> poblarUsuarios()
	{
		List<Usuario> usuarios=Arrays.asList(new Usuario("Rene","Orozco","rene@gmail.com"),new Usuario("Raul","Orozco","raul@gmail.com")
				,new Usuario("Roberto","Orozco","roberto@gmail.com"));
		return usuarios;
	}
	
}
