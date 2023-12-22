package com.domi.spring.security.postgresql.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.domi.spring.security.postgresql.models.User;
import com.domi.spring.security.postgresql.repository.UserRepository;



@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/domi")
public class UsuarioController {
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping
	public List<User> getAll() {
		return userRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public Optional<User> findById(@PathVariable("id")long id){
		return userRepository.findById(id);
	}
	
	@PostMapping("/post")
	public void save(@RequestBody User usuario){
		userRepository.save(usuario);
	}
	
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable("id")long id){
		userRepository.deleteById(id);
	}
	
//	@PutMapping("/update/{id}")
//	public User update(@PathVariable("id") int id, @RequestBody User usuarioActualizado){
//	    // Primero, encuentra el usuario existente
//	    User usuarioExistente = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//
//	    // Luego, actualiza las propiedades del usuario existente con las del usuario actualizado
//	    usuarioExistente.setNombre(usuarioActualizado.getNombre());
//	    usuarioExistente.setApellidos(usuarioActualizado.getApellidos());
//	    usuarioExistente.setEmail(usuarioActualizado.getEmail());
//	    usuarioExistente.setContrasena(usuarioActualizado.getContrasena());
//	    usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
//
//	    // Finalmente, guarda el usuario existente con las propiedades actualizadas
//	    return userRepository.update(usuarioExistente);
//	}
	
	

}
