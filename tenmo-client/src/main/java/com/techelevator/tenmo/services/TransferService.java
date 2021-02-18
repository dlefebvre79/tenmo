package com.techelevator.tenmo.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;

public class TransferService
{

	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	private AuthenticatedUser user = null;

	public TransferService(String url)
	{
		this.BASE_URL = url;
	}
	
	public void setUser(AuthenticatedUser user)
	{
		this.user = user;
	}
	
	public Transfer createTransfer(Transfer transfer)
	{
		Transfer newTransfer = null;
		if(this.user !=null)
		{
			try
			{
			newTransfer = restTemplate.postForObject(BASE_URL, makeTransferEntity(transfer)
												, Transfer.class);
			}
			catch (RestClientResponseException e)
			{
				
			}
		}
		return newTransfer;
	}

	public Transfer updateTransfer(Transfer transfer)
	{
		Transfer updatedTransfer = null;
		if(this.user !=null)
		{
			try
			{
			updatedTransfer = restTemplate.exchange(BASE_URL + "/update/" + transfer.getId(), HttpMethod.PUT,
					makeTransferEntity(transfer), Transfer.class).getBody();
			}
			catch (RestClientResponseException e)
			{
				
			}
		}
		return updatedTransfer;
	}

	public List<Transfer> getAllTransfers()
	{
		List<Transfer> transfers = null;
		if(this.user !=null)
		{
			try
			{
			Transfer[] transferArray = restTemplate.exchange(BASE_URL, HttpMethod.GET,
					makeAuthEntity(), Transfer[].class).getBody();
			transfers = Arrays.asList(transferArray);
			}
			catch (RestClientResponseException e)
			{
				
			}
		}
		return transfers;
	}

	public List<Transfer> getPendingTransfers()
	{
		List<Transfer> transfers = null;
		List<Transfer> pending = new ArrayList<Transfer>();
		if(this.user !=null)
		{
			try
			{
				Transfer[] transferArray = restTemplate.exchange(BASE_URL, HttpMethod.GET,
						makeAuthEntity(), Transfer[].class).getBody();
				transfers = Arrays.asList(transferArray);
				for (Transfer transfer : transfers)
				{
					if(transfer.getTransferStatus().equalsIgnoreCase("pending")
							&& transfer.getUserFrom().equalsIgnoreCase(this.user.getUser().getUsername()))
					{
						pending.add(transfer);
					}
				}
			}
			catch (RestClientResponseException e)
			{
				
			}
		}
		return pending;
	}

	
	public Transfer getById(int id)
	{
		Transfer transfer = null;
		if(this.user !=null)
		{
			try
			{
			transfer = restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.GET,
					makeAuthEntity(), Transfer.class).getBody();
			}
			catch (RestClientResponseException e)
			{
				
			}
		}
		return transfer;
	}
	
	private HttpEntity makeAuthEntity()
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(this.user.getToken());
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
	
	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(this.user.getToken());
		HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}

}
