package com.jros.springboot.reactor.app;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jros.springboot.reactor.app.model.Comentarios;
import com.jros.springboot.reactor.app.model.Usuario;
import com.jros.springboot.reactor.app.model.UsuarioComentarios;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		ejemploIterable();
		ejemploContrapresion();
	}
	
	
	public void ejemploContrapresion() {
		Flux.range(1, 10)
		.log()
		.limitRate(5)
		.subscribe(/*new Subscriber<Integer>() {
			private Subscription s;
			private int limite = 5;
			private int consumido = 0;
			@Override
			public void onSubscribe(Subscription s) {
				this.s=s;
				s.request(limite);
			}

			@Override
			public void onNext(Integer t) {
				log.info(t.toString());
				consumido++;
				if(consumido== limite) {
					consumido=0;
				}
				s.request(limite);
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				
			}
		}*/);
	}

	public void ejemploIntervaloDesdeCreate() {
		Flux.create(emitter -> {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
			private Integer contador=0;
				@Override
				public void run() {
					emitter.next(++contador);
					if(contador == 10) {
						timer.cancel();
						emitter.complete();
					}
					if(contador==5) {
						timer.cancel();
						emitter.error(new InterruptedException("Ha llegado a 5!!"));
					}
				}
			}, 1000, 1000);
		})
//		.doOnNext(next -> log.info(next.toString()))
//		.doOnComplete(() -> log.info("Hemos terminado!!"))
		.subscribe(next -> log.info(next.toString()) , error -> log.error(error.getMessage()), ()-> log.info("Hemos terminado"));
	}
	
	public void ejemploIntervaloInfinito() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		Flux.interval(Duration.ofSeconds(1))
		.doOnTerminate(()-> latch.countDown())
		.flatMap(i -> {
			if(i >=5) {
				return Flux.error(new InterruptedException("Solo hasta 5"));
			}else {
				return Flux.just(i);
			}
		})
		.map(i -> "Hola " +i)
		.retry(2)
//		.doOnNext(s -> log.info(s))
		.subscribe(s -> log.info(s) , error -> log.error(error.getMessage()));
		latch.await();
	}
	public void ejemploDelay() throws InterruptedException {
		Flux<Integer> rango = Flux.range(1, 12)
		.delayElements(Duration.ofSeconds(1))
		.doOnNext( r -> log.info( r.toString()  ) );
		rango.subscribe();
		Thread.sleep(13000);
	}
	public void ejemploInterval() {
		Flux<Integer> rango = Flux.range(1, 12);
		Flux<Long> retraso = Flux.interval(Duration.ofSeconds(1));
		rango.zipWith(retraso, (ra, re) -> ra)
		.doOnNext(r -> log.info(r.toString()))
		.blockLast();
		
	}
	
	public void ejemploUsuarioConComentariosZipWithRango() {
		Flux.just(1, 2, 3, 4)
		.map(i -> (i * 2 ))
		.zipWith(Flux.range(0, 4), (uno, dos) -> String.format("Primer Flux :  %d , Segundo Flux : %d", uno, dos))
		.subscribe(texto -> log.info(texto));
		
	}
	
	public void ejemploUsuarioConComentariosZipWithForma2() {
		Mono<Usuario> usuarioMono=Mono.fromCallable(()-> new Usuario("Joe","Doe"));
		Mono<Comentarios> comentariosMono= Mono.fromCallable(()->{
			Comentarios comentarios = new Comentarios();
			comentarios.addComentarios("Hola este es el comentario 1");
			comentarios.addComentarios("Hola este es el comentario 2");
			comentarios.addComentarios("Hola este es el comentario 3");
			comentarios.addComentarios("Hola este es el comentario 4");
			return comentarios;
		});
		Mono<UsuarioComentarios> usuarioConComentarios=usuarioMono
				.zipWith(comentariosMono)
				.map(tuple ->{
					Usuario u = tuple.getT1();
					Comentarios c = tuple.getT2();
					return new UsuarioComentarios(u,c);
				});
		usuarioConComentarios.subscribe(uc -> log.info(uc.toString()));
	}
	public void ejemploUsuarioConComentariosZipWith() {
		Mono<Usuario> usuarioMono=Mono.fromCallable(()-> new Usuario("Joe","Doe"));
		Mono<Comentarios> comentariosMono= Mono.fromCallable(()->{
			Comentarios comentarios = new Comentarios();
			comentarios.addComentarios("Hola este es el comentario 1");
			comentarios.addComentarios("Hola este es el comentario 2");
			comentarios.addComentarios("Hola este es el comentario 3");
			comentarios.addComentarios("Hola este es el comentario 4");
			return comentarios;
		});
		Mono<UsuarioComentarios> usuarioConComentarios=usuarioMono.zipWith(comentariosMono,(usuario, comentariosUsuariosMono) -> new UsuarioComentarios(usuario,comentariosUsuariosMono));
		usuarioConComentarios.subscribe(uc -> log.info(uc.toString()));
	}
	public void ejemploUsuarioConComentariosFlatMap() {
		Mono<Usuario> usuarioMono=Mono.fromCallable(()-> new Usuario("Joe","Doe"));
		Mono<Comentarios> comentariosMono= Mono.fromCallable(()->{
			Comentarios comentarios = new Comentarios();
			comentarios.addComentarios("Hola este es el comentario 1");
			comentarios.addComentarios("Hola este es el comentario 2");
			comentarios.addComentarios("Hola este es el comentario 3");
			comentarios.addComentarios("Hola este es el comentario 4");
			return comentarios;
		});
		
		usuarioMono.flatMap( u -> comentariosMono.map( c -> new UsuarioComentarios(u, c) ) )
		.subscribe(uc -> log.info(uc.toString()));
	}
	
	
	public void ejemploCollectList() throws Exception {
		List<Usuario> usuarioList = new ArrayList<>();
		usuarioList.add(new Usuario("Andres","Guzman"));
		usuarioList.add(new Usuario("Rene","Orozco"));
		usuarioList.add(new Usuario("Raul" ,"SantaMarina"));
		usuarioList.add(new Usuario("Pedro" ,"Garcia"));
		usuarioList.add(new Usuario("Bruce" ,"Lee"));
		usuarioList.add(new Usuario("Bruce", "Willis"));
		Flux.fromIterable(usuarioList)
		.collectList()
		.subscribe(lista -> {
			lista.forEach(item -> log.info(item.toString()));
		});
	}	
	
	public void ejemploToString() throws Exception {
		List<Usuario> usuarioList = new ArrayList<>();
		usuarioList.add(new Usuario("Andres","Guzman"));
		usuarioList.add(new Usuario("Rene","Orozco"));
		usuarioList.add(new Usuario("Raul" ,"SantaMarina"));
		usuarioList.add(new Usuario("Pedro" ,"Garcia"));
		usuarioList.add(new Usuario("Bruce" ,"Lee"));
		usuarioList.add(new Usuario("Bruce", "Willis"));
		Flux.fromIterable(usuarioList)
				.map(nombre -> nombre.getNombre().toUpperCase().concat(" ").concat(nombre.getApellido().toUpperCase()))
				.flatMap(nombre ->{
					if(nombre.contains("bruce".toUpperCase())) {
						return Mono.just(nombre);
					}else {
						return Mono.empty();
					}
				})
				
				.doOnNext(elemento -> {
					System.out.println(elemento);
					if (elemento == null) {
						throw new RuntimeException("Los Nombres no pueden ser vacíos");
					}
				}).map(nombre -> {
					
					return nombre.toLowerCase();
				}).subscribe( u -> log.info(u));
	}
	public void ejemploFaltMap() throws Exception {
		List<String> usuarioList = new ArrayList<>();
		usuarioList.add("Andres Guzman");
		usuarioList.add("Rene Orozco");
		usuarioList.add("Raul SantaMarina");
		usuarioList.add("Pedro Garcia");
		usuarioList.add("Bruce Lee");
		usuarioList.add("Bruce Willis");
		Flux.fromIterable(usuarioList)
		.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
		.flatMap(usuario ->{
			if(usuario.getNombre().equalsIgnoreCase("bruce")) {
				return Mono.just(usuario);
			}else {
				return Mono.empty();
			}
		})
		
		.doOnNext(elemento -> {
			System.out.println(elemento.getNombre().concat(" ").concat(elemento.getApellido()));
			if (elemento == null) {
				throw new RuntimeException("Los Nombres no pueden ser vacíos");
			}
		}).map(usuario -> {
			String nombre = usuario.getNombre().toLowerCase();
			String apellido = usuario.getApellido().toLowerCase();
			usuario.setNombre(nombre);
			usuario.setApellido(apellido);
			return usuario;
		}).subscribe( u -> log.info(u.toString()));
	}
	public void ejemploIterable() throws Exception {
		List<String> usuarioList = new ArrayList<>();
		usuarioList.add("Andres Guzman");
		usuarioList.add("Rene Orozco");
		usuarioList.add("Raul SantaMarina");
		usuarioList.add("Pedro Garcia");
		usuarioList.add("Bruce Lee");
		usuarioList.add("Bruce Willis");
		Flux<String> nombres=Flux.fromIterable(usuarioList);
		Flux<Usuario> usuarios = nombres
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getNombre().toLowerCase().equals("bruce")).doOnNext(elemento -> {
					System.out.println(elemento.getNombre().concat(" ").concat(elemento.getApellido()));
					if (elemento == null) {
						throw new RuntimeException("Los Nombres no pueden ser vacíos");
					}
				})
//el map es para hacer tranformaciones podemos tener varias transformaciones esta es la segunda
				.map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					String apellido = usuario.getApellido().toLowerCase();
					usuario.setNombre(nombre);
					usuario.setApellido(apellido);
					return usuario;
				}); // si lo dejamos asi automaticamente hace el return con la función de flecha
		// (->)
		nombres.subscribe(elemento -> log.info(elemento), error -> log.error(error.getMessage()), new Runnable() {
			@Override
			public void run() {
				log.info("Ha finalizado la ejecución del Flux con exito!!");
			}
		});
		
		usuarios.subscribe(u -> log.info(u.toString()), error -> log.error(error.getMessage()), new Runnable() {
			
			@Override
			public void run() {
				log.info("Ha finalizado la ejecución del Flux Modificado con exito!!");
				
			}
		});
	}
	
	
	public void ejemplo1DeFlux() {
		Flux<String> nombres = Flux.just("Andres Guzman", "Rene Orozco", "Raul SantaMarina", "Pedro Garcia",
				"Bruce Lee", "Bruce Willis");
//		.map(nombre -> { //el map es para hacer tranformaciones siempre resgresa algo con en este caso otra instancia de un FLUX con los datos modificados
//					return nombre.toUpperCase();
//		})
		Flux<Usuario> usuarios = nombres
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getNombre().toLowerCase().equals("bruce")).doOnNext(elemento -> {
					System.out.println(elemento.getNombre().concat(" ").concat(elemento.getApellido()));
					if (elemento == null) {
						throw new RuntimeException("Los Nombres no pueden ser vacíos");
					}
				})
				// el map es para hacer tranformaciones podemos tener varias transformaciones
				// esta es la segunda
				.map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					String apellido = usuario.getApellido().toLowerCase();
					usuario.setNombre(nombre);
					usuario.setApellido(apellido);
					return usuario;
				}); // si lo dejamos asi automaticamente hace el return con la función de flecha
					// (->)
		nombres.subscribe(elemento -> log.info(elemento), error -> log.error(error.getMessage()), new Runnable() {
			@Override
			public void run() {
				log.info("Ha finalizado la ejecución del Flux con exito!!");
			}
		});

		usuarios.subscribe(u -> log.info(u.toString()), error -> log.error(error.getMessage()), new Runnable() {

			@Override
			public void run() {
				log.info("Ha finalizado la ejecución del Flux Modificado con exito!!");

			}
		});
	}

}
