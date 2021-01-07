package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafka;

import com.example.demo.dao.UbungRepositorie;
import com.example.demo.entities.Ubung;
import com.example.demo.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
@EnableKafka
public class Ubung3MicroserviceApplication implements CommandLineRunner {

	@Autowired
	private UbungRepositorie ubungRepositorie;
	
	public static void main(String[] args) {
		SpringApplication.run(Ubung3MicroserviceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		ubungRepositorie.save(new Ubung(null,"Stuhl-Liegestütze",null,"mit hilfe eines Stuhls liegestütze machen","sehr langsam arbeiten","oft","Stühle","Montag","DOnnerstag","Bären :-)",true));
		ubungRepositorie.save(new Ubung(null,"Treppen schritte",null,"Stelle dich mit dem linken Fuß auf die zweite Treppenstufe, bevor du den rechten Fuß mit einem langen Ausfallschritt nach hinten setzt","sehr langsam arbeiten","oft","Treppen","Montag","DOnnerstag","Stark man :-)",true));
		ubungRepositorie.save(new Ubung(null,"Seitheben mit flaschen",null,"In jeder Hand eine gefüllte PET-Flasche. Die Arme langsam seitlich auf Schulterhöhe heben, Spannung kurz halten, dann wieder senken.","sehr langsam arbeiten","oft","Stühle","Montag","DOnnerstag","Bären :-)",true));
		ubungRepositorie.save(new Ubung(null,"Rudern mit flaschen",null,"Rechtes Knie und rechte Hand auf einer Bank ablegen, Flasche mit links zur Brust. Rücken gerade, Ellbogen am Körper, zurück.","sehr langsam arbeiten","oft","Stühle","Montag","DOnnerstag","Bären :-)",true));
		
		for (Ubung ubung : ubungRepositorie.findAll()) {
			System.out.println(ubung.toString());
		}
		
	}

}
