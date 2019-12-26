package com.jros.springboot.di.app;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.jros.springboot.di.app.models.domain.ItemFactura;
import com.jros.springboot.di.app.models.domain.Producto;
import com.jros.springboot.di.app.models.services.IServicio;
import com.jros.springboot.di.app.models.services.MiServicio;
import com.jros.springboot.di.app.models.services.MiServicioComplejo;

@Configuration
public class AppConfig {
	
	@Bean("miServicioSimple")
	public IServicio registrarMiServicio() {
		return new MiServicio();
	}
	
	@Bean("MiServicioComplejo")
	@Primary
	public IServicio registrarMiServicioComplejo() {
		return new MiServicioComplejo();
	}
	
	@Bean("itemsFactura")
	public List<ItemFactura> registrarItems()
	{
		Producto producto1 = new Producto("Camara Sony",100);
		Producto producto2 = new Producto("Monitor LG",800);
		Producto producto3 = new Producto("XBOX ONE",600);
		
		ItemFactura item1= new ItemFactura(producto1,2);
		ItemFactura item2= new ItemFactura(producto2,5);
		ItemFactura item3= new ItemFactura(producto3,4);
		
		return Arrays.asList(item1,item2,item3);
	}
	@Bean("itemsFacturaOficina")
	public List<ItemFactura> registrarItemsOficina()
	{
		Producto producto1 = new Producto("Moto G 6",300);
		Producto producto2 = new Producto("Monitor SONY",900);
		Producto producto3 = new Producto("PLAY STATION 4",700);
		Producto producto4 = new Producto("Escritorio Oficina ",500);
		
		ItemFactura item1= new ItemFactura(producto1,2);
		ItemFactura item2= new ItemFactura(producto2,1);
		ItemFactura item3= new ItemFactura(producto3,3);
		ItemFactura item4= new ItemFactura(producto4,6);
		
		return Arrays.asList(item1,item2,item3,item4);
	}
	

}
