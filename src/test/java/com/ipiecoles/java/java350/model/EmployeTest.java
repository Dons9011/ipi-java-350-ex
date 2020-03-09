package com.ipiecoles.java.java350.model;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
//		"1,'T12345', 0, 0.5, 500.0",
//		"1,'M12345', 0, 1.0, 1700.0",
//		"2,'T12345', 0, 1.0, 2300.0",

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
	
	
	
	
	
}

