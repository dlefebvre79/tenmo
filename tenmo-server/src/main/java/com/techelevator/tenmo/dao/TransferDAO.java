package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO
{
	public Transfer get(int id);
	public Transfer create(Transfer transfer);
	public Transfer update(Transfer transfer);
}
