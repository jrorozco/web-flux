package com.jros.springboot.webflux.apirest.app.controllers;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.jros.springboot.webflux.apirest.app.models.documents.Producto;
import com.jros.springboot.webflux.apirest.app.models.service.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/api/productos")
public class ProductoController {
	
	@Autowired
	private ProductoService productoService;
	
	@Value("${config.uplodas.path}")
	private String path;
	
	@PostMapping("/v2")
	public Mono<ResponseEntity<Producto>> guardarProductoFoto(Producto producto, @RequestPart FilePart file)
	{
		if(producto.getCreateAt() ==null) {
			producto.setCreateAt(new Date());
		}
		producto.setFoto(UUID.randomUUID().toString() + "-" +file.filename()
		.replace(" ", "")
		.replace(":", "")
		.replace("\\", ""));
		return file.transferTo(new File(path + producto.getFoto())).then( productoService.save(producto)).map( p -> ResponseEntity.created( URI.create( "/api/productos/"+p.getId() ) )
																	  .contentType( MediaType.APPLICATION_JSON_UTF8 )
																	  .body( p ) );
																	  
	}
	
	
	@GetMapping
	public Mono< ResponseEntity<Flux<Producto>>> listar()
	{
		return Mono.just(
				ResponseEntity.ok()
							  .contentType(MediaType.APPLICATION_JSON_UTF8)	
							  .body(productoService.findAll())
				);
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Producto>> ver(@PathVariable String id){
		return productoService.findById(id).map(p -> ResponseEntity.ok()
																  .contentType(MediaType.APPLICATION_JSON_UTF8)
																  .body(p))
										   .defaultIfEmpty(ResponseEntity.notFound().build());		
		
	}
	
	
	/**
	 * 
	 * @param monoProducto
	 * @return
	 */
	@PostMapping
	public Mono<ResponseEntity<Map<String, Object>>> guardarProducto(@Valid @RequestBody Mono<Producto> monoProducto)// tenemos que recibir un Mono<Producto> para que sea observable
	{//el tipo de dato que regresamos es un Mono<ResponseEntity<Map<String, Object>>> donde regresaresmo los mensajes de error o de exito!!
		/*EN ESTA PARTE VAMOS A MANEJAR LOS ERRORES PARA REGRESALOS EN UN JSON...
		 * 
		 */
		Map<String, Object> respuesta = new HashMap<String, Object>();// En este map almacenaremos las respuestas de error o correctos
		return monoProducto.flatMap(producto -> { // con el FlatMap convertimos el flujo de producto en  un Map con los mensajes
				if(producto.getCreateAt() ==null) {
					producto.setCreateAt(new Date());
				}
			return productoService.save(producto).map(p -> { //con el map transformamos producto en un map de respuesta 
				respuesta.put("producto", p);
				respuesta.put("mensaje", "Producto Guardado con exito");
				respuesta.put("timestamp", new Date());
				return ResponseEntity.created( URI.create( "/api/productos/"+p.getId() ) )
						.contentType( MediaType.APPLICATION_JSON_UTF8 )
						.body( respuesta );
			});
		}).onErrorResume(t -> { // si tiene error regresa una clase con excepcion de los errores WebExchangeBindException.class
			return Mono.just(t).cast(WebExchangeBindException.class) // t este es un objeto lanzable para exceptiones por eso lo tenemos que convertir a un Mono list
					   .flatMap( e -> Mono.just(e.getFieldErrors()) ) // trasnformamos en Mono List 
					   .flatMapMany( errors -> Flux.fromIterable(errors)) // convertimos a de Mono  A Flux con FlatMapMany 
					   .map(fielderror -> "El campo " + fielderror.getField() + " " +fielderror.getDefaultMessage()) // con el map convertimos cada fieldError  a tipo String
					   .collectList() // lo convertimos a un Mono
					   .flatMap(list -> { // ahora que ya es un Mono lo vamos a convertir a un tipo ResponseEntity
						   	respuesta.put("errors ", list);
							respuesta.put("timestamp", new Date());
							respuesta.put("status", HttpStatus.BAD_REQUEST.value());
							return Mono.just(ResponseEntity.badRequest().body(respuesta));
					   });
		});
																	  
	}
	
	@PostMapping("/upload/{id}")
	public Mono<ResponseEntity<Producto>> upload(@PathVariable String id, @RequestPart FilePart file){
		return productoService.findById(id).flatMap(p -> {
			p.setFoto(UUID.randomUUID().toString() + "-" +file.filename()
			.replace(" ", "")
			.replace(":", "")
			.replace("\\", ""));
			return  file.transferTo(new File(path + p.getFoto())).then( productoService.save(p));
		}).map(p -> ResponseEntity.ok(p))
		  .defaultIfEmpty(ResponseEntity.notFound().build());		
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Producto>> editar(@RequestBody Producto producto,@PathVariable String id){
		return productoService.findById(id).flatMap(p -> {
			   								p.setNombre(producto.getNombre());
			   								p.setPrecio(producto.getPrecio());
			   								p.setCreateAt(producto.getCreateAt());
			   								p.setCategoria(producto.getCategoria());
											return productoService.save(p);})
										   .map( p -> ResponseEntity.created( URI.create( "/api/productos/".concat( p.getId() )  )  )
												   					.contentType(MediaType.APPLICATION_JSON_UTF8)
												   					.body(p))
										   .defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id){
		return productoService.findById(id).flatMap(p -> {
			return productoService.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
		}).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}

}
