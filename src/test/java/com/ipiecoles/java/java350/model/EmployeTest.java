package com.ipiecoles.java.java350.model;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.ipiecoles.java.java350.model.Employe;

public class EmployeTest {
	
	@Test
	public void testAncienneDateEmbaucheAujourdhui() {
		//given 
		Employe employe = new Employe();
		employe.setDateEmbauche(LocalDate.now());
		//When
		Integer nbAnnees =employe.getNombreAnneeAnciennete();
		//Then 
		Assertions.assertThat(nbAnnees).isEqualTo(0);
	}
	
	
	@Test
	public void testAncienneDateEmbaucheNull() {
		//given 
		Employe employe = new Employe();
		employe.setDateEmbauche(null);
		//When
		Integer nbAnnees =employe.getNombreAnneeAnciennete();
		//Then 
		Assertions.assertThat(nbAnnees).isEqualTo(0);
	}
	
	
	@ParameterizedTest
	@CsvSource({
		"1,'T12345', 0, 1.0, 1000.0"


	})
	public void testGetPrimeAnnuelle(Integer performance, String matricule, Integer nbAnneesAnciennete, Double tempsPartiel, Double prime) {
		
		//given 
		Employe employe = new Employe();
		employe.setDateEmbauche(LocalDate.now().minusYears(nbAnneesAnciennete));
		employe.setMatricule(matricule);
		employe.setPerformance(performance);
		employe.setTempsPartiel(tempsPartiel);
		
		//When
		Double primeCalculee = employe.getPrimeAnnuelle();
		
		//Then 
		Assertions.assertThat(primeCalculee).isEqualTo(prime);
	}
	
	

	@Test
	public void testAugmenterSalaireAvecSalaireNull(){
		
		//given 
		Double pourcentage = 0.654;
		Employe employe = new Employe();
		employe.setSalaire(null);
		employe.augmenterSalaire(pourcentage);

		//When
		Double salaire = employe.getSalaire();
		
		//Then 
		Assertions.assertThat(salaire).isEqualTo(null);
	}
	
	
	@Test
	public void testAugmenterSalaireAvec2Null(){
		
		//given 
		Double pourcentage = null;
		Employe employe = new Employe();
		employe.setSalaire(null);
		employe.augmenterSalaire(pourcentage);

		//When
		Double salaire = employe.getSalaire();
		
		//Then 
		Assertions.assertThat(salaire).isEqualTo(null);
	}
	
	@Test
	public void testAugmenterSalaireAvecPourcentageNegatif(){
		
		//given 
		Double pourcentage = -0.654;
		Employe employe = new Employe();
		employe.setSalaire(2000.9);
		employe.augmenterSalaire(pourcentage);

		//When
		Double salaire = employe.getSalaire();
		
		//Then 
		Assertions.assertThat(salaire).isEqualTo(2000.9);
	}
	
	
	
	
}

