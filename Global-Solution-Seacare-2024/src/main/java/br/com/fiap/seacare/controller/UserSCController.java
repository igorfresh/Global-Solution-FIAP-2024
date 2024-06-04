package br.com.fiap.seacare.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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

    @Autowired
    PagedResourcesAssembler<UserSC> pageAssembler;

    @GetMapping
    public PagedModel<EntityModel<UserSC>> index(
        @RequestParam(required = false) String name,
        @PageableDefault(size = 3, sort = "name", direction = Direction.ASC) Pageable pageable
    ) {
        Page<UserSC> page = null;
        page = repository.findAll(pageable);
        return pageAssembler.toModel(page);
    }

    @PostMapping
    public ResponseEntity<UserSC> create(@RequestBody @Valid UserSC userSC) {
        repository.save(userSC);

        return ResponseEntity
        .created(userSC.toEntityModel().getRequiredLink("self").toUri())
        .body(userSC);

    }

    @GetMapping("{id}")
    public EntityModel<UserSC> show (@PathVariable Long id) {
        var userSC = repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("usuário não encontrado")
        );

        return userSC.toEntityModel();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("usuário não encontrado")
        );

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
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
