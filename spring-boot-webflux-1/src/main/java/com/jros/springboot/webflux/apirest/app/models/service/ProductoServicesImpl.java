package com.jros.springboot.webflux.apirest.app.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jros.springboot.webflux.apirest.app.models.dao.CategoriaDao;
import com.jros.springboot.webflux.apirest.app.models.dao.ProductoDao;
import com.jros.springboot.webflux.apirest.app.models.documents.Categoria;
import com.jros.springboot.webflux.apirest.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServicesImpl implements ProductoService{
	
	@Autowired
	private ProductoDao productoDao;
	
	
	@Autowired
	private CategoriaDao categoriaDao;

	@Override
	public Flux<Producto> findAll() {
		
		return productoDao.findAll();
	}

	@Override
	public Mono<Producto> findById(String id) {
		
		return productoDao.findById(id);
	}

	@Override
	public Mono<Producto> save(Producto producto) {
		
		 return productoDao.save(producto);
	}

	@Override
	public Mono<Void> delete(Producto producto) {
		
		return productoDao.delete(producto);
	}

	@Override
	public Flux<Producto> findAllConNombreUpperCase() {
		return productoDao.findAll().map(p ->{ 
			p.setNombre(p.getNombre().toUpperCase());
			return p;
			});
	}

	@Override
	public Flux<Producto> findAllConNombreUpperCaseRepeat() {
		
		return productoDao.findAll()
				.map(p ->{
					p.setNombre(p.getNombre().toUpperCase());
					return p;
				}).repeat(5000);
	}

	@Override
	public Flux<Categoria> findAllCategoria() {
		return categoriaDao.findAll();
	}

	@Override
	public Mono<Categoria> findCategoriaById(String id) {
		return categoriaDao.findById(id);
	}

	@Override
	public Mono<Categoria> saveCategoria(Categoria categoria) {

		return categoriaDao.save(categoria);
	}


}
