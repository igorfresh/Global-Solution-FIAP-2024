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
import jakarta.validation.Valid;

@RestController
@RequestMapping("reportedArtifacts")
public class ReportedArtifactController {
    
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ReportedArtifactsRepository repository;

    @Autowired
    PagedResourcesAssembler<ReportedArtifacts> pageAssembler;

    @GetMapping
    public PagedModel<EntityModel<ReportedArtifacts>> index(
        @RequestParam(required = false) Long id,
        @PageableDefault(size = 3, sort = "id", direction = Direction.ASC) Pageable pageable
    ) {
        Page<ReportedArtifacts> page = null;
        page = repository.findAll(pageable);
        return pageAssembler.toModel(page);
    }

    @PostMapping
    public ResponseEntity<ReportedArtifacts> create(@RequestBody @Valid ReportedArtifacts reportedArtifacts) {
        repository.save(reportedArtifacts);

        return ResponseEntity
        .created(reportedArtifacts.toEntityModel().getRequiredLink("self").toUri())
        .body(reportedArtifacts);

    }

    @GetMapping("{id}")
    public EntityModel<ReportedArtifacts> show (@PathVariable Long id) {
        var reportedArtifacts = repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("denúncia não encontrada")
        );

        return reportedArtifacts.toEntityModel();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("denúncia não encontrada")
        );

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
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
