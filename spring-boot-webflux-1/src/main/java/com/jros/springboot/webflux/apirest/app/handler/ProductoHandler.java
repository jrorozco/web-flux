package com.jros.springboot.webflux.apirest.app.handler;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jros.springboot.webflux.apirest.app.models.documents.Producto;
import com.jros.springboot.webflux.apirest.app.models.service.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductoHandler {
	
	@Autowired
	private ProductoService productoService;
	
	@Value("${config.uplodas.path}")
	private String path;
	
	
	@Autowired
	private Validator validator;
	
	public Mono<ServerResponse> upload(ServerRequest request)
	{
		String id =request.pathVariable("id");
		return  request.multipartData().map(multipart -> multipart.toSingleValueMap().get("file"))
				       .cast(FilePart.class)
				       .flatMap( file -> productoService.findById(id)
				    		   	.flatMap(p -> {
				    		   		p.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
				    		   															.replace(" ","-")
				    		   															.replace(":","")
				    		   															.replace("\\",""));
				    		   		return file.transferTo( new File( path + p.getFoto() ) ).then(productoService.save(p));
				        })).flatMap(p -> ServerResponse
				        		         .created( URI.create( "/api/v2/productos/".concat( p.getId() ) ) )
				        		         .contentType(MediaType.APPLICATION_JSON_UTF8)
				        		         .body(BodyInserters.fromObject(p))
				        		         ).switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> listar( ServerRequest request)
	{
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(productoService.findAll(), Producto.class);
	}
	
	public Mono<ServerResponse> ver(ServerRequest request){
		String id =request.pathVariable("id"); //recibimos la variable por le URL.
		return productoService.findById(id).flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
				                                                            .body(BodyInserters.fromObject(p)) )
										   .switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> crear(ServerRequest request){
		Mono<Producto> producto = request.bodyToMono(Producto.class); // Convertimos el request del producto a Mono<Producto>
		return producto.flatMap(p -> {
			
			//para enviar los errores en texto de la peticion
			Errors errors=new BeanPropertyBindingResult(p, Producto.class.getName());  // importamos de Errors Spring
			validator.validate(p, errors);// validamos el Objeto que nos esta llegando por la peticion y si tiene errores se los pasamos a errors
			//preguntamos si  errors contiene errores
			if(errors.hasErrors()) {
				// si tiene errores los manejamos aqui!!
				//Tenemos los errores pero tenesmo que iterarlos para convertirlos a Sting
				return Flux.fromIterable(errors.getFieldErrors()) // los tenemos un un Flux hay que iterarlos con el map
							.map(fieldError -> "El campo " + fieldError.getField() + " " +fieldError.getDefaultMessage()) // ya los convertimos a Flux string
							.collectList() // los regresamos a un Mono de strings
							.flatMap(list -> ServerResponse.badRequest().body(BodyInserters.fromObject(list))); // ahora tenemos una lista y lo convertimos en un Mono<ServerResponse> y le pasamos la lista de errores al body
				
			}else {
//				si no tiene errores procesamos la respuesta
				
				//le agregamos la fecha por default
				if(p.getCreateAt() == null) {
					p.setCreateAt(new Date());
				}
				// regresamos el el mono con  el producto convertirdo a Mono<ServerResponse> 
				return productoService.save(p).flatMap( pdb -> ServerResponse
				         .created( URI.create( "/api/v2/productos/".concat( pdb.getId() ) ) )
				         .contentType(MediaType.APPLICATION_JSON_UTF8)
				         .body(BodyInserters.fromObject(pdb))); // regresamos el Flux del producto
			}
			
		});
		
	}

	
	public Mono<ServerResponse> editar(ServerRequest request){
		Mono<Producto> producto = request.bodyToMono(Producto.class); // botenemos el producto que viene en la peticion en el request
		String id =request.pathVariable("id");  // obtenemos el Id que viene en la peticion URL PathVariable
		Mono<Producto> productoDeLaBD = productoService.findById(id);// nos lo traemos el producto de la BD
		return productoDeLaBD.zipWith(producto, (db, req) ->{
			db.setNombre(req.getNombre());
			db.setPrecio(req.getPrecio());
			db.setCategoria(req.getCategoria());
			return db;
		}).flatMap(p -> ServerResponse.created( URI.create("/api/v2/productos/".concat( p.getId() ) ) )
				                      .contentType(MediaType.APPLICATION_JSON_UTF8)
				                      .body(productoService.save(p),Producto.class)
	     ).switchIfEmpty(ServerResponse.notFound().build());
		
		
	}
	
	public Mono<ServerResponse> eliminar(ServerRequest request){
		String id =request.pathVariable("id");  
		Mono<Producto> productoDeLaBD = productoService.findById(id);
		return productoDeLaBD.flatMap(p -> productoService.delete(p).then(ServerResponse.noContent().build())).switchIfEmpty(ServerResponse.notFound().build());
	}
}
