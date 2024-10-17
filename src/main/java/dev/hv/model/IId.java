package dev.hv.model;

import java.util.UUID;

/**
 * Interface representing an entity with a universally unique identifier (UUID).
 */
public interface IId {

   UUID getId();

   void setId(UUID id);

}
