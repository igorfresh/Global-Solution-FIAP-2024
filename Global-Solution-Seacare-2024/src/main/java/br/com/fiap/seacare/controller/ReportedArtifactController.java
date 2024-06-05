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

import br.com.fiap.seacare.model.ReportedArtifacts;
import br.com.fiap.seacare.repository.ReportedArtifactsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("reportedArtifacts")
@Tag(name = "Artefatos Reportados", description = "Artefatos inseridos na denúncia pelos usuários")
public class ReportedArtifactController {
    
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ReportedArtifactsRepository repository;

    @Autowired
    PagedResourcesAssembler<ReportedArtifacts> pageAssembler;

    @GetMapping
    @Operation(
        summary = "Listar Artefatos Reportados",
        description = "Retorna uma lista paginada de artefatos reportados."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artefatos reportados listados"),
        @ApiResponse(responseCode = "404", description = "Artefatos reportados não encontrados")
    })
    public PagedModel<EntityModel<ReportedArtifacts>> index(
        @RequestParam(required = false) Long id,
        @PageableDefault(size = 3, sort = "id", direction = Direction.ASC) Pageable pageable
    ) {
        Page<ReportedArtifacts> page = null;
        page = repository.findAll(pageable);
        return pageAssembler.toModel(page);
    }

    @PostMapping
    @Operation(
        summary = "Cadastrar Artefato Reportado",
        description = "Cadastra um novo artefato reportado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Artefato reportado criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida")
    })
    public ResponseEntity<ReportedArtifacts> create(@RequestBody @Valid ReportedArtifacts reportedArtifacts) {
        repository.save(reportedArtifacts);

        return ResponseEntity
        .created(reportedArtifacts.toEntityModel().getRequiredLink("self").toUri())
        .body(reportedArtifacts);

    }

    @GetMapping("{id}")
    @Operation(
        summary = "Buscar Artefato Reportado",
        description = "Recupera um artefato reportado pelo ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artefato reportado encontrado"),
        @ApiResponse(responseCode = "404", description = "Artefato reportado não encontrado")
    })
    public EntityModel<ReportedArtifacts> show (@PathVariable Long id) {
        var reportedArtifacts = repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("denúncia não encontrada")
        );

        return reportedArtifacts.toEntityModel();
    }

    @DeleteMapping("{id}")
    @Operation(
        summary = "Excluir Artefato Reportado",
        description = "Remove um artefato reportado pelo ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Artefato reportado excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Artefato reportado não encontrado")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("denúncia não encontrada")
        );

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @Operation(
        summary = "Atualizar Artefato Reportado",
        description = "Atualiza um artefato reportado existente pelo ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artefato reportado atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Artefato reportado não encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida")
    })
    public ReportedArtifacts update(@PathVariable Long id, @RequestBody ReportedArtifacts reportedArtifacts) {
        log.info("atualizando artefatos reportados com id {}", id, reportedArtifacts);

        verifyExistingArtifact(id);

        reportedArtifacts.setId(id);
        return repository.save(reportedArtifacts);
    }

    private void verifyExistingArtifact(Long id) {
        repository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe artefatos reportados com o id informado. Consulte lista em /artifact"));
    }

}
