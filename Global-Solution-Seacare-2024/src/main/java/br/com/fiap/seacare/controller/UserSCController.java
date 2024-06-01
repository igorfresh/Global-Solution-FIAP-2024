package br.com.fiap.seacare.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.seacare.model.UserSC;
import br.com.fiap.seacare.repository.UserSCRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("userSc")
public class UserSCController {
    
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    UserSCRepository repository;

    @GetMapping
    public Page<UserSC> index(
        @RequestParam(required = false) String name,
        @PageableDefault(size = 3, sort = "name", direction = Direction.ASC) Pageable pageable
    ) {
        return repository.findAll(pageable);
    }

    @PostMapping
    public UserSC create(@RequestBody @Valid UserSC userSc) {
        log.info("cadastrando usuário {}", userSc);
        return repository.save(userSc);

    }

    @GetMapping("{id}")
    public ResponseEntity<UserSC> show (@PathVariable Long id) {
        log.info("buscando usuário por id {}", id);
        return repository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        log.info("Apagando usuário com id {}");
        verifyExistingUserSC(id);

        repository.deleteById(id);
    }

    @PutMapping("{id}")
    public UserSC update(@PathVariable Long id, @RequestBody UserSC userSC) {
        log.info("atualizando usuário com id {}", id, userSC);

        verifyExistingUserSC(id);

        userSC.setId(id);
        return repository.save(userSC);
    }

    private void verifyExistingUserSC(Long id) {
        repository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe usuário com o id informado. Consulte lista em /userSc"));
    }

}
