package com.jros.springboot.api.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/variables")
public class EjemplosVariablesRutaController {
	
	
	@GetMapping("/")
	public String index(Model model)
	{
		model.addAttribute("titulo", "Enviar parametros de la ruta (@pathVariable)");
		return "variables/index";
	}
	
	@GetMapping("/string/{texto}")
	public String varibales(@PathVariable String texto,Model model)
	{
		model.addAttribute("titulo", "Recibir parametros de la ruta (@pathVariable)");
		model.addAttribute("resultado","Texto enviado a la ruta  es "+ texto);
		return "variables/ver";
	}
	
	@GetMapping("/string/{texto}/{numero}")
	public String varibales(@PathVariable String texto,@PathVariable Integer numero,Model model)
	{
		model.addAttribute("titulo", "Recibir parametros de la ruta (@pathVariable)");
		model.addAttribute("resultado","Texto enviado a la ruta  es "+ texto + " el numero enviado en el path es " +numero);
		return "variables/ver";
	}

}
