package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class User
{

	private Integer id;
	private String username;
	private BigDecimal balance;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public BigDecimal getBalance()
	{
		return balance;
	}
	
	public void setBalance(BigDecimal balance)
	{
		this.balance = balance;
	}
	
	@Override
	public String toString()
	{
		return "User ID: " + this.getId() + ", Username: " + this.getUsername();
	}
}