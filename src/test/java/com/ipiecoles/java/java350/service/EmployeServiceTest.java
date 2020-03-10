package com.ipiecoles.java.java350.service;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {
	
	@InjectMocks
	private EmployeService employeService;
	
	@Mock
	private EmployeRepository employeRepository;
	
	@Test
	public void testEmbaucheEmployeCommercialPleinTempsBTS() throws EmployeException {
		
		//Given 
		String nom = "Doe";
		String prenom = "John";
		Poste poste = Poste.COMMERCIAL;
		NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
		Double tempsPartiel = 1.0;
		//On impose le résultat que renvoie la méthode findLastMatricule  :
		Mockito.when(employeRepository.findLastMatricule()).thenReturn("00345");
		// On impose le résultat que renvoie la méthode findLastMatricule findByMatricule  : 
		Mockito.when(employeRepository.findByMatricule("C00346")).thenReturn(null);

		
		
		//When
		employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
		
		//Then 
		//on simule un enregistrement en bdd d'un nouvel employe
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeCaptor.capture());
        Employe employe = employeCaptor.getValue();
        
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getMatricule()).isEqualTo("C00346");
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());	
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(tempsPartiel);	
        Assertions.assertThat(employe.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);	
        //on met le calcul du salaire en dur pour ne pas copier coller la methode de calcul du service : 1521.22*1.2*1.0
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);	
	}
	
	@Test
	public void testEmbaucheEmployeLimiteMatricule() throws EmployeException {
		
		//Given 
		String nom = "Doe";
		String prenom = "John";
		Poste poste = Poste.COMMERCIAL;
		NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
		Double tempsPartiel = 1.0;
		//On impose le résultat que renvoie la méthode findLastMatricule  :
		Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");
		
		//When//Then en AssertJ
		Assertions.assertThatThrownBy(() -> {
			employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
		}).isInstanceOf(EmployeException.class).hasMessage("Limite des 100000 matricules atteinte !");
		
		//ou sinon 
		//When
		try {
			employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
			Assertions.fail("erreur ça aurait dû planter !");
		} catch (Exception e) {
			//on vérifie que l'exception soulevée est la bonne et que le message est le bon
			//Then 
			Assertions.assertThat(e).isInstanceOf(EmployeException.class);
			Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
		}
		
	}
	
}
