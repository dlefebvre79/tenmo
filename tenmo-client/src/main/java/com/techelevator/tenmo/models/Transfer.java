package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer
{
	private int id;
	private String transferType;
	private String transferStatus;
	private String userFrom;
	private String userTo;
	private BigDecimal amount;
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getTransferType()
	{
		return transferType;
	}

	public void setTransferType(String transferType)
	{
		this.transferType = transferType;
	}

	public String getTransferStatus()
	{
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus)
	{
		this.transferStatus = transferStatus;
	}

	public String getUserFrom()
	{
		return userFrom;
	}

	public void setUserFrom(String userFrom)
	{
		this.userFrom = userFrom;
	}

	public String getUserTo()
	{
		return userTo;
	}

	public void setUserTo(String userTo)
	{
		this.userTo = userTo;
	}

	public BigDecimal getAmount()
	{
		return amount;
	}

	public void setAmount(BigDecimal amount)
	{
		this.amount = amount;
	}

	@Override
	public String toString()
	{
		return "Transfer ID: " + this.getId() + ", Amount: $" + this.getAmount() + ", From: " + this.getUserFrom() + " To: " + this.getUserTo();
	}
}
