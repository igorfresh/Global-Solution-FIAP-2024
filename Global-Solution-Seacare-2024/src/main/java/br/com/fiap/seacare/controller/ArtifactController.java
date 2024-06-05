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

import br.com.fiap.seacare.model.Artifact;
import br.com.fiap.seacare.repository.ArtifactRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("artifact")
@Tag(name = "Artefato", description = "Artefatos encontrados nas praias")
public class ArtifactController {
    
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ArtifactRepository repository;

    @Autowired
    PagedResourcesAssembler<Artifact> pageAssembler;

    @GetMapping
    @Operation(
        summary = "Listar Artefatos",
        description = "Retorna uma lista paginada de artefatos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artefatos listados"),
        @ApiResponse(responseCode = "404", description = "Artefatos não encontrados")
    })
    public PagedModel<EntityModel<Artifact>> index(
        @RequestParam(required = false) String name,
        @PageableDefault(size = 3, sort = "name", direction = Direction.ASC) Pageable pageable
    ) {
        Page<Artifact> page = null;
        page = repository.findAll(pageable);
        return pageAssembler.toModel(page);
    }

    @PostMapping
    @Operation(
        summary = "Cadastrar Artefato",
        description = "Cadastra um novo artefato."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Artefato criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida")
    })
    public ResponseEntity<Artifact> create(@RequestBody @Valid Artifact artifact) {
        repository.save(artifact);

        return ResponseEntity
        .created(artifact.toEntityModel().getRequiredLink("self").toUri())
        .body(artifact);

    }

    @GetMapping("{id}")
    @Operation(
        summary = "Buscar Artefato",
        description = "Recupera um artefato pelo ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artefato encontrado"),
        @ApiResponse(responseCode = "404", description = "Artefato não encontrado")
    })
    public EntityModel<Artifact> show (@PathVariable Long id) {
        var artifact = repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("artefato não encontrado")
        );

        return artifact.toEntityModel();
    }

    @DeleteMapping("{id}")
    @Operation(
        summary = "Excluir Artefato",
        description = "Remove um artefato pelo ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Artefato excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Artefato não encontrado")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("artefato não encontrado")
        );

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @Operation(
        summary = "Atualizar Artefato",
        description = "Atualiza um artefato existente pelo ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artefato atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Artefato não encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida")
    })
    public Artifact update(@PathVariable Long id, @RequestBody Artifact artifact) {
        log.info("atualizando artefato com id {}", id, artifact);

        verifyExistingArtifact(id);

        artifact.setId(id);
        return repository.save(artifact);
    }

    private void verifyExistingArtifact(Long id) {
        repository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe artefato com o id informado. Consulte lista em /artifact"));
    }

}
