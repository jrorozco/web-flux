package com.jros.springboot.webflux.app.controller;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.jros.springboot.webflux.app.models.documents.Categoria;
import com.jros.springboot.webflux.app.models.documents.Producto;
import com.jros.springboot.webflux.app.service.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class ProductoController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoService productoService;
	
	@Value("${config.uplodas.path}")
	private  String RUTA_ARCHIVOS;
	
	@GetMapping({"/listar","/"})
	public Mono<String> listar(Model model) {
		Flux<Producto> productos= productoService.findAllConNombreUpperCase();
		productos.subscribe(p -> log.info(p.getNombre()));
		model.addAttribute("titulo","Listado de Productos");
		model.addAttribute("productos",productos);
		
		return Mono.just("listar");
	}
	
	@ModelAttribute("categorias")
	public Flux<Categoria>  categorias(){
		return productoService.findAllCategoria();
	}
	
	@GetMapping("/form")
	public Mono<String> crear(Model model){
		
		model.addAttribute("producto",new Producto());
		model.addAttribute("titulo","Registro de Productos");
		model.addAttribute("btn_value","Crear");
		return Mono.just("form");
	}
	
	@PostMapping("/form")
	public Mono<String> guardar(@Valid Producto producto, BindingResult result,@RequestPart FilePart file, Model model){
		//Si tiene errores regresa el error al formulario 
		if(result.hasErrors()) {
			model.addAttribute("btn_value","Crear"); // con este atributo le ponemos el valor al boton segun si es guardar o actualizar
			model.addAttribute("titulo","Registro de Productos");// con este atributo mandamos el titutlo al form
			return Mono.just("form");// regresamos al for con los errores
		}
		else 
		{
			 Mono<Categoria> categoria = productoService.findCategoriaById(producto.getCategoria().getId()); // obtenemos elflujo del categoria por id
			 return categoria.flatMap(c -> { // le pasamos la categoria al flujo 
				 if(producto.getCreateAt()==null)
				 {
					 producto.setCreateAt(new Date());// le asignamo la fecha si viene en null al producto
				 }
				 producto.setCategoria(c); // le asignamos la categoria
				 if(!file.filename().isEmpty()) { // si el archivo no esta vacio
					 producto.setFoto(UUID.randomUUID().toString() + "-" + file.filename().replace(" ", "").replace(":", "").replace("\\", ""));//si no es vacio  se lo agregamos a Producto
				 }
				 return productoService.save(producto); // guardamos el producot y le regresamos el flujo con el return (importante poner el return)
			 }).doOnNext(p -> {
				 log.info("Guardasmo la categoria al producto nombre categoria : {} , con el ID :{} ", p.getCategoria().getNombre(), p.getCategoria().getId());
				 log.info("Guardando producto nombre : {} con el id {}" ,p.getNombre(), p.getId());// mostramos en le log el resultado
				 }) 
			  .flatMap(p ->{ //nos llega por parametro del form @RequestPart FilePart file
				  if(!file.filename().isEmpty()) { // checamos que no se vacio
					  log.info("Ruta Completa : {}", RUTA_ARCHIVOS + p.getFoto());
					  return file.transferTo( new File( RUTA_ARCHIVOS + p.getFoto() ) ); // guardamos el archivo en un directorio y el nomrbre se lo ponemos al producto para despues trarlo como un dato
				  }
				  log.info("No entro : {}", RUTA_ARCHIVOS + p.getFoto());
				  return Mono.empty();// si viene vacio regresamos un Mono vacio
			   }) 
			  .thenReturn("redirect:/listar?success=producto+guardado+con+exito");		//y resgresamos un Mono<String> con la ruta y el mensaje redirigiendo a listar 
		}
	}
	
	@GetMapping("/form-v2/{id}")
	public Mono<String> editarV2(@PathVariable String id, Model model){
		return productoService.findById(id).doOnNext(p ->{ 
			log.info("Producto : " + p.getNombre());
			model.addAttribute("titulo" , "Editando Producto : " + p.getNombre());
			model.addAttribute("producto",p);
			model.addAttribute("btn_value","Actualizar");
		})
		.defaultIfEmpty(new Producto())
		.flatMap( p -> {
			if(p.getId() == null) {
				return Mono.error(new InterruptedException("No existe el producto que desea editar"));
			}
			return Mono.just(p);
		})
		.then(Mono.just("/form"))
		.onErrorResume(ex -> Mono.just("redirect:/listar?error=no+exist+el+producto+que+dese+editar"));
	}
	
	@GetMapping("/ver/{id}")
	public Mono<String> ver(Model model, @PathVariable String id){
		return productoService.findById(id)
							  .doOnNext(p -> {
								  model.addAttribute("titulo", "Detalles del Producoto : " + p.getNombre());
								  model.addAttribute("producto", p);})
							  .flatMap(p -> {
								  if(p.getId() == null) {
									  return Mono.error(new InterruptedException("El detalle del producto a consutar no existe"));
								  }
								  return Mono.just(p);})
							  .then(Mono.just("ver"))
							  .onErrorResume(ex -> Mono.just("redirect:/listar?error=no+exist+el+producto+que+desea+ver"));
	}
	
	@GetMapping("/uploads/foto/{nombreFoto:.+}")
	public Mono<ResponseEntity<Resource>> verFoto(@PathVariable String nombreFoto) throws MalformedURLException
	{
		Path rutaPath=Paths.get(RUTA_ARCHIVOS).resolve(nombreFoto).toAbsolutePath();
		Resource imagen = new UrlResource(rutaPath.toUri());
		return Mono.just(
			ResponseEntity.ok()
			             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imagen.getFilename() + "\"")
			             .body(imagen)
				);
	}
	
	@GetMapping("/form/{id}")
	public Mono<String> editar(@PathVariable String id, Model model){
		Mono<Producto> productoMono=productoService.findById(id).doOnNext(p -> log.info("Producto : " + p.getNombre())).defaultIfEmpty(new Producto());
		model.addAttribute("titulo" , "Editando Producto");
		model.addAttribute("producto",productoMono);
		model.addAttribute("btn_value","Actualizar");
		return Mono.just("/form");
	}
	
	@GetMapping("/eliminar/{id}")
	public Mono<String> eliminar(@PathVariable String id){
		return productoService.findById(id)
		.defaultIfEmpty(new Producto()) // primero mandamos un producto vacio si esta vacio o no existe
		.flatMap( p-> {// verficamos si existe el produto en la base de datos...
			if(p.getId() ==null) {
				return Mono.error(new InterruptedException("No exixte el producto que desea eliminar"));
			}
			return Mono.just(p);
		}).flatMap(p -> {
			
			log.info("Eliminando el producto : " + p.getNombre() + " con el ID : " +p.getId());
			return productoService.delete(p);
			
		}).then(Mono.just("redirect:/listar?success=El+producto+se+elimino+correctamente"))
		.onErrorResume(ex -> Mono.just("redirect:/listar?error=El+producto+a+eliminar+no+se+existe!"))	;
		
		
	}
	
	
	@GetMapping("/listar-datadriver")
	public String listarDataDriver(Model model) {
		Flux<Producto> productos= productoService.findAllConNombreUpperCase().delayElements(Duration.ofSeconds(1));
		productos.subscribe(p -> log.info(p.getNombre()));
		model.addAttribute("titulo","Listado de Productos");
		model.addAttribute("productos", new ReactiveDataDriverContextVariable( productos,2));
		
		return "listar";
	}
	
	@GetMapping("/listar-full")
	public String listarFull(Model model) {
		Flux<Producto> productos= productoService.findAllConNombreUpperCaseRepeat();
//		productos.subscribe(p -> log.info(p.getNombre()));
		model.addAttribute("titulo","Listado de Productos");
		model.addAttribute("productos",productos);
		
		return "listar";
	}
	@GetMapping("/listar-chunked")
	public String listarChunked(Model model) {
		Flux<Producto> productos= productoService.findAllConNombreUpperCaseRepeat();
//		productos.subscribe(p -> log.info(p.getNombre()));
		model.addAttribute("titulo","Listado de Productos");
		model.addAttribute("productos",productos);
		
		return "listar-chunked";
	}
	

}
