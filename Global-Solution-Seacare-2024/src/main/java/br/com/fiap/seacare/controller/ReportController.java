package br.com.fiap.seacare.controller;

import java.time.LocalDate;

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

import br.com.fiap.seacare.model.Report;
import br.com.fiap.seacare.repository.ReportRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("report")
public class ReportController {
    
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ReportRepository repository;

   @Autowired
    PagedResourcesAssembler<Report> pageAssembler;

    @GetMapping
    public PagedModel<EntityModel<Report>> index(
        @RequestParam(required = false) LocalDate date,
        @PageableDefault(size = 3, sort = "date", direction = Direction.ASC) Pageable pageable
    ) {
        Page<Report> page = null;
        page = repository.findAll(pageable);
        return pageAssembler.toModel(page);
    }

    @PostMapping
    public ResponseEntity<Report> create(@RequestBody @Valid Report report) {
        repository.save(report);

        return ResponseEntity
        .created(report.toEntityModel().getRequiredLink("self").toUri())
        .body(report);

    }

    @GetMapping("{id}")
    public EntityModel<Report> show (@PathVariable Long id) {
        var location = repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("denúncia não encontrada")
        );

        return location.toEntityModel();
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
    public Report update(@PathVariable Long id, @RequestBody Report report) {
        log.info("atualizando denúncia com id {}", id, report);

        verifyExistingReport(id);

        report.setId(id);
        return repository.save(report);
    }

    private void verifyExistingReport(Long id) {
        repository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe denúncia com o id informado. Consulte lista em /report"));
    }

}