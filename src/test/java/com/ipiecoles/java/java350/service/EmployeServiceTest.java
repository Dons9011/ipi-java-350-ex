package com.ipiecoles.java.java350.service;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
	
	
	@Test
	public void testCalculPerformanceCommercialExceptions1() throws EmployeException {
		
		//Given 		
		String matricule = "C12345";
		Long caTraite = null;
		Long objectifCa = (long) 2000;
		
		//When//Then en AssertJ
		Assertions.assertThatThrownBy(() -> {
			employeService.calculPerformanceCommercial( matricule,  caTraite,  objectifCa);
		}).isInstanceOf(EmployeException.class).hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");
	}
	
	
	@Test
	public void testCalculPerformanceCommercialExceptions1bis() throws EmployeException {
		
		//Given 		
		String matricule = "C12345";
		Long caTraite = -15l;
		Long objectifCa = (long) 2000;
		
		//When//Then en AssertJ
		Assertions.assertThatThrownBy(() -> {
			employeService.calculPerformanceCommercial( matricule,  caTraite,  objectifCa);
		}).isInstanceOf(EmployeException.class).hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");
	}
	
	
	@Test
	public void testCalculPerformanceCommercialExceptions2() throws EmployeException {
		
		//Given 		
		String matricule = "C12345";
		Long caTraite = 1575l;
		Long objectifCa = null;
		
		//When//Then en AssertJ
		Assertions.assertThatThrownBy(() -> {
			employeService.calculPerformanceCommercial( matricule,  caTraite,  objectifCa);
		}).isInstanceOf(EmployeException.class).hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
	}
	
	
	@Test
	public void testCalculPerformanceCommercialExceptions2bis() throws EmployeException {
		
		//Given 		
		String matricule = "C12345";
		Long caTraite = 1575l;
		Long objectifCa = -567l;
		
		//When//Then en AssertJ
		Assertions.assertThatThrownBy(() -> {
			employeService.calculPerformanceCommercial( matricule,  caTraite,  objectifCa);
		}).isInstanceOf(EmployeException.class).hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
	}

	
	@Test
	public void testCalculPerformanceCommercialExceptions3() throws EmployeException {
		
		//Given 		
		String matricule = null;
		Long caTraite = 1575l;
		Long objectifCa = 567l;
		
		//When//Then en AssertJ
		Assertions.assertThatThrownBy(() -> {
			employeService.calculPerformanceCommercial( matricule,  caTraite,  objectifCa);
		}).isInstanceOf(EmployeException.class).hasMessage("Le matricule ne peut être null et doit commencer par un C !");
	}
	
	
	
	@Test
	public void testCalculPerformanceCommercialExceptions3bis() throws EmployeException {
		
		//Given 		
		String matricule = "L12345";
		Long caTraite = 1575l;
		Long objectifCa = 567l;
		
		//When//Then en AssertJ
		Assertions.assertThatThrownBy(() -> {
			employeService.calculPerformanceCommercial( matricule,  caTraite,  objectifCa);
		}).isInstanceOf(EmployeException.class).hasMessage("Le matricule ne peut être null et doit commencer par un C !");
	}

	
	@Test
	public void testCalculPerformanceCommercialExceptions4() throws EmployeException {
		//Given 		
		String matricule = "C12345";
		Long caTraite = 1575l;
		Long objectifCa = 567l;
		Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(null);
		//When//Then en AssertJ
		Assertions.assertThatThrownBy(() -> {
			employeService.calculPerformanceCommercial( matricule,  caTraite,  objectifCa);
		}).isInstanceOf(EmployeException.class).hasMessage("Le matricule " + matricule + " n'existe pas !");
	}
	
	
	@ParameterizedTest
	@CsvSource({
		"1,'C12345', 900, 1000",
		"1,'C12345', 1000, 1000",
		"3,'C12345', 1100, 1000",
		"6,'C12345', 1300, 1000",

	})
	public void testCalculPerformanceCommercialCas2etAutres(Integer perf, String matric, Long caTraite, Long objectifCa) throws EmployeException{
		//Given 		
	     String nom = "Doe";
	     String prenom = "John";
	     String matricule = matric;
	     LocalDate dateEmbauche = LocalDate.now();
	     Double salaire = Entreprise.SALAIRE_BASE;
	     Integer performance = 1;
	     Double tempsPartiel = 1.0;
	     
	     Employe employe = new Employe (nom,prenom,matricule, dateEmbauche,salaire,performance,tempsPartiel);
	     
	     Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(1d);
	     Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(employe);


	     //when 
	     employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
	     
		//then 

	     ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
	     Mockito.verify(employeRepository, Mockito.times(1)).save(employeCaptor.capture());
	     Employe emp = employeCaptor.getValue();

        Assertions.assertThat(emp.getPerformance()).isEqualTo(perf);

	}

		


	
	




	
	

	
	

	
	
	
}
