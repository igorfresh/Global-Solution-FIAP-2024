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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("userSc")
@Tag(name = "usuário Sea Care", description = "Usuários que farão a denúncia")
public class UserSCController {
    
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    UserSCRepository repository;

    @Autowired
    PagedResourcesAssembler<UserSC> pageAssembler;

    @GetMapping
    @Operation(
        summary = "Listar Usuários",
        description = "Retorna uma lista paginada de usuários."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuários listados"),
        @ApiResponse(responseCode = "404", description = "Usuários não encontrados")
    })
    public PagedModel<EntityModel<UserSC>> index(
        @RequestParam(required = false) String name,
        @PageableDefault(size = 3, sort = "name", direction = Direction.ASC) Pageable pageable
    ) {
        Page<UserSC> page = null;
        page = repository.findAll(pageable);
        return pageAssembler.toModel(page);
    }

    
    @PostMapping
    @Operation(
        summary = "Cadastrar Usuário",
        description = "Cadastra um novo usuário."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida")
    })
    public ResponseEntity<UserSC> create(@RequestBody @Valid UserSC userSC) {
        repository.save(userSC);

        return ResponseEntity
        .created(userSC.toEntityModel().getRequiredLink("self").toUri())
        .body(userSC);

    }

    @GetMapping("{id}")
    @Operation(
        summary = "Buscar Usuário",
        description = "Recupera um usuário pelo ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public EntityModel<UserSC> show (@PathVariable Long id) {
        var userSC = repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("usuário não encontrado")
        );

        return userSC.toEntityModel();
    }

    @DeleteMapping("{id}")
    @Operation(
        summary = "Excluir Usuário",
        description = "Remove um usuário pelo ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("usuário não encontrado")
        );

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @Operation(
        summary = "Atualizar Usuário",
        description = "Atualiza um usuário existente pelo ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida")
    })
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
