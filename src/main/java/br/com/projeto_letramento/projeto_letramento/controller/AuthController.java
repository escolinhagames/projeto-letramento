package br.com.projeto_letramento.projeto_letramento.controller;


import br.com.projeto_letramento.projeto_letramento.dto.LoginRequestDTO;
import br.com.projeto_letramento.projeto_letramento.dto.RegisterRequestDTO;
import br.com.projeto_letramento.projeto_letramento.dto.ResponseDTO;
import br.com.projeto_letramento.projeto_letramento.infra.security.TokenService;

import br.com.projeto_letramento.projeto_letramento.model.ProfessorModel;
import br.com.projeto_letramento.projeto_letramento.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final ProfessorRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        ProfessorModel professorModel = this.repository.findOptionalByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(body.senha(), professorModel.getSenha())) {
            String token = this.tokenService.generateToken(professorModel);
            return ResponseEntity.ok(new ResponseDTO(professorModel.getNome(), token));
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        Optional<ProfessorModel> professorModel = this.repository.findOptionalByEmail(body.email());

        if(professorModel.isEmpty()) {
            ProfessorModel newProfessorModel = new ProfessorModel();
            newProfessorModel.setSenha(passwordEncoder.encode(body.senha()));
            newProfessorModel.setEmail(body.email());
            newProfessorModel.setNome(body.nome());
            this.repository.save(newProfessorModel);

            String token = this.tokenService.generateToken(newProfessorModel);
            return ResponseEntity.ok(new ResponseDTO(newProfessorModel.getNome(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
