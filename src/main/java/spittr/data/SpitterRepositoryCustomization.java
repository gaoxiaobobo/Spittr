package spittr.data;
import spittr.entity.Spitter;

public interface SpitterRepositoryCustomization{
	Spitter findByUsername(String username);
}