package com.jros.springboot.webflux.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jros.springboot.webflux.app.dao.ProductoDao;
import com.jros.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {
private static final Logger log = LoggerFactory.getLogger(ProductoRestController.class);
	
	@Autowired
	private ProductoDao productoDao;
	
	@GetMapping()
	public Flux<Producto> index(){
		Flux<Producto> productos= productoDao.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				}).doOnNext(p -> log.info(p.toString()));
		return productos;
	}
	
	@GetMapping("/{id}")
	public Mono<Producto> showById(@PathVariable String id){
		
//		Mono<Producto> producto = productoDao.findById(id);
		Flux<Producto> productos= productoDao.findAll();
		Mono<Producto> producto =productos.filter(p -> p.getId().equals(id)).next().doOnNext(p -> log.info(p.toString()));
		return producto;
	}

}
