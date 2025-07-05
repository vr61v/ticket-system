package org.vr61v.services;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Abstract base service providing CRUD (Create, Read, Update, Delete) operations
 * for entities with both single and batch processing support.
 * <p>
 * This service delegates persistence operations to the underlying Spring Data repository
 * and provides transactional boundaries for all operations.
 *
 * @param <E>  the entity type this service manages
 * @param <ID> the type of entity's identifier (supports both single and compound keys)
 */
@Transactional
public abstract class CrudService<E, ID> {

    private final ListCrudRepository<E, ID> repository;

    public CrudService(ListCrudRepository<E, ID> repository) {
        this.repository = repository;
    }

    public E create(E entity) {
        return repository.save(entity);
    }

    public E update(E entity) {
        return repository.save(entity);
    }

    public Optional<E> findById(ID id) {
        return repository.findById(id);
    }

    public List<E> findAll() {
        return repository.findAll();
    }

    public void delete(ID id) {
        repository.deleteById(id);
    }

}
