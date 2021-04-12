package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.exception.ResourceNotFoundException;

import java.util.List;

/**
 * CRUD for a generic service.
 *
 * @param <D> DTO type parameter.
 * @param <E> Entity type parameter.
 */

public interface BaseService<D, E> {

    D create(D dto);
    List<D> findAll() throws ResourceNotFoundException;
    D findById(Long id) throws ResourceNotFoundException;
    D update(D dto) throws ResourceNotFoundException;
    void delete(Long id) throws ResourceNotFoundException;
}
