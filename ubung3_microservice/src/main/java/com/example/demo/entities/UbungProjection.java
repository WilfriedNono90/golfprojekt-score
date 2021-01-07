package com.example.demo.entities;

//@Projection(name = "standard", types = Ubung.class)
public interface UbungProjection {
	public String getName();
	public String getAufgabe();
	public Boolean getOnline();
}
