package com.jros.springboot.webflux.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.jros.springboot.webflux.app.models.documents.Categoria;
import com.jros.springboot.webflux.app.models.documents.Producto;
import com.jros.springboot.webflux.app.service.ProductoService;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner{
	
	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);
	
	@Autowired
	private ProductoService productoServices;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		
		mongoTemplate.dropCollection("categoria").subscribe();
		Categoria electronico = new Categoria("electronica");
		Categoria muebles = new Categoria("muebles");
		Categoria deportes = new Categoria("deportes");
		Categoria hogar = new Categoria("hogar");
		Categoria ferreteria = new Categoria("ferreteria");
		mongoTemplate.dropCollection("producto").subscribe();
		
		Flux.just(electronico,muebles,deportes,hogar,ferreteria) // creamos el Flux para las categorias
			.flatMap(productoServices::saveCategoria) // guardamos en la BD de mongo las categorias
			.doOnNext(c -> log.info("Insertando Categoria ID : {} Nombre : {}  ",c.getId(), c.getNombre() ))// imprimimos cada categoria
		    .thenMany(// -- GUARDAMOS LOS PRODUCTOS CON SUS RESPECTIVAS CATEGORIAS
		          Flux.just( new Producto("Pantalla LG 42' plana",1345.00,electronico),
		    				new Producto("Mesa para Pantalla 48' plana",1045.00,muebles),
		    				new Producto("Balon de Americano",14005.00,deportes),
		    				new Producto("Licuadora LG 6 vel",13845.00,hogar),
		    				new Producto("Taladro roto martillo 1/2",19345.00,ferreteria),
		    				new Producto("Pantalla LG 72' plana",17345.00,electronico) )
		    		.flatMap(producto -> {
		    					producto.setCreateAt(new Date());
		    					return productoServices.save(producto);
		    		} )
		    		
		    		).subscribe(p -> log.info("Insert : {}" , p)); // IMPRIMIMOS EL LOG DE LOS PRODUCTOS			
		
		
		
	}

}
