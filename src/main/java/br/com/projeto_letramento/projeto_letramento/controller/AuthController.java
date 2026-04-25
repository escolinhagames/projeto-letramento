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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final ProfessorRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO body){
        String senha = body.senha();
        if (senha == null || senha.isBlank() || body.email() == null || body.email().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        ProfessorModel professorModel = this.repository.findOptionalByEmail(body.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(senha, professorModel.getSenha())) {
            String token = this.tokenService.generateToken(professorModel);
            return ResponseEntity.ok(new ResponseDTO(professorModel.getNome(), token, professorModel.getId()));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody RegisterRequestDTO body){
        String nome = body.nome();
        String senha = body.senha();
        if (nome == null || nome.isBlank() || senha == null || senha.isBlank() || body.email() == null || body.email().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<ProfessorModel> professorModel = this.repository.findOptionalByEmail(body.email());

        if(professorModel.isEmpty()) {
            ProfessorModel newProfessorModel = new ProfessorModel();
            newProfessorModel.setSenha(passwordEncoder.encode(senha));
            newProfessorModel.setEmail(body.email());
            newProfessorModel.setNome(nome);
            this.repository.save(newProfessorModel);

            String token = this.tokenService.generateToken(newProfessorModel);
            return ResponseEntity.ok(new ResponseDTO(newProfessorModel.getNome(), token, newProfessorModel.getId()));
        }
        return ResponseEntity.badRequest().build();
    }
}
