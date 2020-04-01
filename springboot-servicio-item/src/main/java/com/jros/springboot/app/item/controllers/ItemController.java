package com.jros.springboot.app.item.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jros.springboot.app.item.models.Item;
import com.jros.springboot.app.item.models.services.ItemService;

@RestController
public class ItemController {
	
	@Autowired
	private ItemService itemService;

	@GetMapping("/listar")
	public List<Item> listar(){
		return itemService.findAll();
	}
	
	@GetMapping("/listar/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad)
	{
		return itemService.findById(id, cantidad);
	}
}
