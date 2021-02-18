package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.exceptions.InsufficientFundsException;
import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@Component
public class TransferSqlDAO implements TransferDAO
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private UserSqlDAO userDao;
	
	@Override
	public Transfer get(int id) throws TransferNotFoundException
	{
		Transfer transfer = new Transfer();
		int accountFrom;
		int accountTo;
		User userFrom = new User();
		User userTo = new User();
		
		String sql = "SELECT t.transfer_id "
					+ "		, t.account_from "
					+ "		, t.account_to "
					+ "		, tt.transfer_type_desc AS type "
					+ "		, ts.transfer_status_desc AS status "
					+ "		, t.amount "
					+ "FROM transfers AS t "
					+ "JOIN transfer_types AS tt "
					+ "		ON t.transfer_type_id = tt.transfer_type_id "
					+ "JOIN transfer_statuses AS ts "
					+ "		ON t.transfer_status_id = ts.transfer_status_id "
					+ "WHERE t.transfer_id = ?; ";
		
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, id);
		
		if (row.next())
		{
			accountFrom = row.getInt("account_from");
			accountTo = row.getInt("account_to");
			
			userFrom = userDao.getUserByAccount(accountFrom);
			userTo = userDao.getUserByAccount(accountTo);
			
			transfer = mapRowToTransfer(row, userFrom.getUsername(), userTo.getUsername());
			return transfer;
		}
		throw new TransferNotFoundException();
	}
	
	@Override
	public Transfer create(Transfer transfer) throws InsufficientFundsException
	{
		int userFrom = userDao.findIdByUsername(transfer.getUserFrom());
		int userTo = userDao.findIdByUsername(transfer.getUserTo());
		int accountFrom = userDao.getAccountByUserId(userFrom);
		int accountTo = userDao.getAccountByUserId(userTo);
		int transferType = getTypeId(transfer.getTransferType());
		int transferStatus = getStatusId(transfer.getTransferStatus());
		BigDecimal amount = transfer.getAmount();
		BigDecimal startBalanceFrom = userDao.getBalanceById(userFrom);
		BigDecimal startBalanceTo = userDao.getBalanceById(userTo);
		Transfer newTransfer = null;
		
		if(transfer.getTransferType().equalsIgnoreCase("send"))
		{
			if(startBalanceFrom.compareTo(amount) >= 0)
			{
				int transferId = getNextId();
				String sql = "INSERT INTO transfers "
							+ "(transfer_id"
							+ ", transfer_type_id"
							+ ", transfer_status_id"
							+ ", account_from"
							+ ", account_to"
							+ ", amount) "
							+ "VALUES "
							+ "(?, ?, ?, ?, ?, ?);";
				
				jdbcTemplate.update(sql, transferId, transferType, transferStatus,
										accountFrom, accountTo, amount);
				
				userDao.updateBalance(userFrom, startBalanceFrom.subtract(amount));
				userDao.updateBalance(userTo, startBalanceTo.add(amount));
				
				newTransfer = get(transferId);
			}
			else
			{
				throw new InsufficientFundsException();
			}
		}
		else if(transfer.getTransferType().equalsIgnoreCase("request"))
		{
			int transferId = getNextId();
			String sql = "INSERT INTO transfers "
						+ "(transfer_id"
						+ ", transfer_type_id"
						+ ", transfer_status_id"
						+ ", account_from"
						+ ", account_to"
						+ ", amount) "
						+ "VALUES "
						+ "(?, ?, ?, ?, ?, ?);";
			
			jdbcTemplate.update(sql, transferId, transferType, transferStatus,
									accountFrom, accountTo, amount);
			
			newTransfer = get(transferId);
		}
		return newTransfer;	
		
	}

	@Override
	public Transfer update(Transfer transfer) throws InsufficientFundsException
	{
		int transferId = transfer.getId();
		int userFrom = userDao.findIdByUsername(transfer.getUserFrom());
		int userTo = userDao.findIdByUsername(transfer.getUserTo());
		int transferType = getTypeId(transfer.getTransferType());
		int transferStatus = getStatusId(transfer.getTransferStatus());
		BigDecimal amount = transfer.getAmount();
		BigDecimal startBalanceFrom = userDao.getBalanceById(userFrom);
		BigDecimal startBalanceTo = userDao.getBalanceById(userTo);
		Transfer updatedTransfer = null;
		
		if(transfer.getTransferStatus().equalsIgnoreCase("approved"))
		{
			if(startBalanceFrom.compareTo(amount) >= 0)
			{
				updateTransferStatus(transferId, transferStatus);
				userDao.updateBalance(userFrom, startBalanceFrom.subtract(amount));
				userDao.updateBalance(userTo, startBalanceTo.add(amount));
				updatedTransfer = get(transferId);
			}
			else
			{
				throw new InsufficientFundsException();
			}
		}
		else if (transfer.getTransferStatus().equalsIgnoreCase("rejected"))
		{
			updateTransferStatus(transferId, transferStatus);
			updatedTransfer = get(transferId);
		}
		return updatedTransfer;	

	}
	
	private void updateTransferStatus(int transferId, int statusId)
	{
		String sql = "UPDATE transfers "
				+ "SET transfer_status_id = ? "
				+ "WHERE transfer_id = ?;";
	
		jdbcTemplate.update(sql, statusId, transferId);
	}
	
 	private int getStatusId(String status)
	{
		int id = -1;
		
		String sql = "SELECT transfer_status_id "
					+ "FROM transfer_statuses "
					+ "WHERE transfer_status_desc = ?;";
		
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, status);
		
		if(row.next())
		{
			id = row.getInt("transfer_status_id");
		}
		
		return id;
	}
	
	private int getTypeId(String type)
	{
		int id = -1;
		
		String sql = "SELECT transfer_type_id "
					+ "FROM transfer_types "
					+ "WHERE transfer_type_desc = ?;";
		
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, type);
		
		if(row.next())
		{
			id = row.getInt("transfer_type_id");
		}
		
		return id;
	}
	
	private Transfer mapRowToTransfer(SqlRowSet row, String userFrom, String userTo)
	{
		Transfer transfer = new Transfer();
		
		transfer.setId(row.getInt("transfer_id"));
		transfer.setUserFrom(userFrom);
		transfer.setUserTo(userTo);
		transfer.setTransferType(row.getString("type"));
		transfer.setTransferStatus(row.getString("status"));
		transfer.setAmount(row.getBigDecimal("amount"));
		
		return transfer;
	}
	
	private int getNextId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_transfer_id') AS next_id");
		if(nextIdResult.next()) {
			return nextIdResult.getInt("next_id");
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new transfer");
		}
	}
}
