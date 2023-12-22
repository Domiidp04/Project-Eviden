package com.domi.spring.security.postgresql.controllers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.domi.spring.security.postgresql.models.User;
import com.domi.spring.security.postgresql.payload.request.UpdateRequest;
import com.domi.spring.security.postgresql.payload.response.MessageResponse;
import com.domi.spring.security.postgresql.repository.UserRepository;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
  
	@Autowired
	private UserRepository userRepository;
	


  @GetMapping("/user/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public Optional<User> findById(@PathVariable("id")long id){
		return userRepository.findById(id);
	}

  //Principal, si es admin muestra todo, sino muestra solo su usuario
  @GetMapping("/user")
  @PreAuthorize("hasRole('MODERATOR') or hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<?> getUser() {
      // Obtener el nombre de usuario del contexto de seguridad
      String username = SecurityContextHolder.getContext().getAuthentication().getName();

      // Obtener los roles del usuario autenticado
      Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

      // Verificar si el usuario tiene el rol 'ADMIN'
      boolean isAdmin = authorities.stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

      if (isAdmin) {
          // Si es admin, mostrar la lista completa de usuarios
          List<User> allUsers = userRepository.findAll();
          return ResponseEntity.ok(allUsers);
      } else {
          // Si es user o moderator, mostrar solo su propio usuario
          Optional<User> user = userRepository.findByUsername(username);

          if (user.isPresent()) {
              return ResponseEntity.ok(user.get());
          } else {
              return ResponseEntity.notFound().build();
          }
      }
  }
  
  @PreAuthorize("hasRole('MODERATOR') or hasRole('USER') or hasRole('ADMIN')")
  @DeleteMapping("/delete/{id}")
	public void delete(@PathVariable("id")long id){
		userRepository.deleteById(id);
	}
  
  @PreAuthorize("hasRole('MODERATOR') or hasRole('USER') or hasRole('ADMIN')")
  @PutMapping("/update/{userId}")
  public ResponseEntity<?> updateUser(@PathVariable Long userId, @Valid @RequestBody UpdateRequest updateRequest) {
      User user = userRepository.findById(userId)
              .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

      // Validar si el nuevo username ya está en uso por otro usuario
      if (!user.getUsername().equals(updateRequest.getUsername()) &&
          userRepository.existsByUsername(updateRequest.getUsername())) {
          return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
      }

      // Actualizar los campos necesarios
      user.setUsername(updateRequest.getUsername());
      user.setEmail(updateRequest.getEmail());
      // Puedes agregar más campos según sea necesario

      userRepository.save(user);

      return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
  }



}
