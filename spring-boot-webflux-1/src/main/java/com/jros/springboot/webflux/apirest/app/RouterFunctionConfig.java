package com.jros.springboot.webflux.apirest.app;

// estos van como estaticos para el manejo de GET
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;// estos van como estaticos para el route

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jros.springboot.webflux.apirest.app.handler.ProductoHandler;

@Configuration
public class RouterFunctionConfig {

	/**
	 * Cambios para ejemplos del GIT ejemplo con RAMA NUEVA
	 * @param handler
	 * @return
	 */
	
	
	@Bean
	public RouterFunction<ServerResponse> routes(ProductoHandler handler)
	{
//		return route( GET("/api/v2/productos").or(GET("/api/v3/productos")), request -> handler.listar(request));
		return route( GET("/api/v2/productos").or(GET("/api/v3/productos")),  handler::listar)// con referencia a metodos
				     .andRoute(GET("/api/v2/productos/{id}"), handler::ver)
				     .andRoute(POST("/api/v2/productos"), handler::crear)
				     .andRoute(PUT("/api/v2/productos/{id}"), handler::editar)
				     .andRoute(DELETE("/api/v2/productos/{id}"), handler::eliminar)
				     .andRoute(POST("/api/v2/productos/upload/{id}"), handler::upload); // agregamos otra ruta
	}
}
