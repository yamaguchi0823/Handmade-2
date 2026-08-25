package com.example.app.controller.api;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.ItemCreateRequest;
import com.example.app.dto.ItemDto;
import com.example.app.dto.ItemUpdateRequest;
import com.example.app.service.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemApiController {
	
	private final ItemService itemService;
	
	@GetMapping
	public List<ItemDto> list(){
		return itemService.listActive();
	}
	
	@GetMapping("/inactive")
	public List<ItemDto> listInactive() {
		return itemService.listInactive();
	}
	
	@PostMapping
	public void create(@RequestBody ItemCreateRequest req) {
		itemService.create(req);
	}
	
	@PutMapping("/{id}")
	public void update(
			@PathVariable long id,
			@RequestBody ItemUpdateRequest req
			) {
		itemService.update(id, req);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		itemService.deactivate(id);
	}
	
	@PutMapping("/{id}/reactive")
	public void reactive(@PathVariable long id) {
		itemService.reactive(id);
	}
}
