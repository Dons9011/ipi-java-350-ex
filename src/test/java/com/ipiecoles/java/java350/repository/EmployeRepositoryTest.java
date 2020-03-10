package com.ipiecoles.java.java350.repository;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.repository.EmployeRepository;


@SpringBootTest
public class EmployeRepositoryTest {

    @Autowired
    EmployeRepository employeRepository;
    
    
    //avant chaque test on vide la BDD comme ça pas de surprise 
    @BeforeEach
    public void setup() {
    	employeRepository.deleteAll();
    }
    
    @Test
    public void testFindLastMatricule(){
        //Given

    	//When
    	String lastMatricule = employeRepository.findLastMatricule();

        //Then
    	Assertions.assertThat(lastMatricule).isNull();
    }
    
    
    @Test
    public void testFindLastMatricule2Employe(){
        //Given
    	Employe employe1 = employeRepository.save(new Employe("DOE", "John", "M12345", LocalDate.now(), 1500d, 1, 1.0));
    	Employe employe2 = employeRepository.save(new Employe("DOE", "Jane", "T01234", LocalDate.now(), 1500d, 1, 1.0));

    	//When
    	String lastMatricule = employeRepository.findLastMatricule();

        //Then
    	Assertions.assertThat(lastMatricule).isEqualTo("12345");
    }
    
    
    
    
    
}