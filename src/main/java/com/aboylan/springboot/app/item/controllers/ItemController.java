package com.aboylan.springboot.app.item.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aboylan.springboot.app.item.models.Item;
import com.aboylan.springboot.app.item.models.Producto;
import com.aboylan.springboot.app.item.models.service.ItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class ItemController {

	@Autowired
	@Qualifier("serviceFeign")
	private ItemService itemService;

	@GetMapping("/listar")
	public List<Item> listar(@RequestParam(name = "nombre") String nombre,
			@RequestHeader(name = "token-request") String token) {
		System.out.println(nombre);
		System.out.println(token);
		return itemService.findAll();
	}

	@HystrixCommand(fallbackMethod = "metodoAlternativo")
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		return itemService.findById(id, cantidad);
	}

	public Item metodoAlternativo(@PathVariable Long id, @PathVariable Integer cantidad) {

		Producto producto = new Producto();
		producto.setId(id);
		producto.setNombre("Camara Sony");
		producto.setPrecio(500.00);

		Item item = new Item();
		item.setCantidad(cantidad);
		item.setProducto(producto);

		return item;
	}
}
