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

import br.com.fiap.seacare.model.Report;
import br.com.fiap.seacare.model.ReportedArtifacts;
import br.com.fiap.seacare.repository.ReportedArtifactsRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("reportedArtifacts")
public class ReportedArtifactController {
    
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ReportedArtifactsRepository repository;

    @GetMapping
    public Page<ReportedArtifacts> index(
        @RequestParam(required = false) Report report,
        @PageableDefault(size = 3, sort = "report", direction = Direction.ASC) Pageable pageable
    ) {
        return repository.findAll(pageable);
    }

    @PostMapping
    public ReportedArtifacts create(@RequestBody @Valid ReportedArtifacts reportedArtifacts) {
        log.info("cadastrando artefatos reportados {}", reportedArtifacts);
        return repository.save(reportedArtifacts);

    }

    @GetMapping("{id}")
    public ResponseEntity<ReportedArtifacts> show (@PathVariable Long id) {
        log.info("buscando artefatos reportados por id {}", id);
        return repository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        log.info("Apagando artefatos reportados com id {}");
        verifyExistingArtifact(id);

        repository.deleteById(id);
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