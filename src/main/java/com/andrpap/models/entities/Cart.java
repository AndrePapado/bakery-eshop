package com.andrpap.models.entities;



public class Cart {
	
	
private int id;

private String name;

private int quantity;

private String price;

private String image;

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}



public String getPrice() {
	return price;
}

public void setPrice(String price) {
	this.price = price;
}

public String getImage() {
	return image;
}

public void setImage(String image) {
	this.image = image;
}

public int getQuantity() {
	return quantity;
}

public void setQuantity(int quantity) {
	this.quantity = quantity;
}

public Cart(int id, String name, int quantity, String price, String image) {
	super();
	this.id = id;
	this.name = name;
	this.quantity = quantity;
	this.price = price;
	this.image = image;
}






}
