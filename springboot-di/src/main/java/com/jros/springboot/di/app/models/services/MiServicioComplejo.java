package com.jros.springboot.di.app.models.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

//@Component("MiServicioComplejo")
//@Primary
public class MiServicioComplejo implements IServicio{

	@Override
	public String operacion() {
		return  "Realizando un operacion importante Complicado";
	}

	
}
