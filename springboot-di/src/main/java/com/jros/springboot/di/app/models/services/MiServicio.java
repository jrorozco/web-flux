package com.jros.springboot.di.app.models.services;

import org.springframework.stereotype.Component;

//@Component("miServicioSimple")
public class MiServicio implements IServicio{

	@Override
	public String operacion() {
		return  "Realizando un operacion importante Simple...";
	}

	
}
