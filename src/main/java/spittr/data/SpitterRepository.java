package spittr.data;

import org.springframework.data.jpa.repository.JpaRepository;

import spittr.entity.Spitter;

public interface SpitterRepository extends JpaRepository<Spitter, Long>, 
										   SpitterRepositoryCustomization{	
}