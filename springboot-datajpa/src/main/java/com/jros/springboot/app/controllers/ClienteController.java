package com.jros.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jros.springboot.app.model.dao.IClienteDao;
import com.jros.springboot.app.model.entity.Cliente;

@Controller
public class ClienteController {
	
	@Autowired
	private IClienteDao clienteDao;
	
	@RequestMapping(value="/listar",method=RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo","Listado de clientes");
		model.addAttribute("clientes",clienteDao.findAll());
		return "listar";
	}

	@RequestMapping(value="/form")
	public String crear(Map<String , Object> model) {
		Cliente cliente =new Cliente();
		model.put("cliente", cliente);
		model.put("titulo","Formulario de Cliente");
		
		return "form";
	}
	
	@RequestMapping(value="/form",method=RequestMethod.POST	)
	public String guardarCliente(@Valid Cliente cliente, BindingResult resultErrores, Model model) { //-- para poder hacer las validaciones del lado del servidor tenemos que anotar con valid nota el BindingResult simpre va al lado del objeto que se va a validar en este caso Cliente.
		if(resultErrores.hasErrors()) { //--BindingResult con este objeto vemos  sí tiene errores el formulario manejaremos los errores.
			model.addAttribute("titulo","Formulario de Cliente");
			return "form"; //-- si tiene errores de validacion resgresamos la vista del formulario con los errores...
		}
		clienteDao.save(cliente);
		return "redirect:listar";
	}
}
