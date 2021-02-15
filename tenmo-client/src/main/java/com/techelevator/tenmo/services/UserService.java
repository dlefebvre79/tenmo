package com.techelevator.tenmo.services;

import java.math.BigDecimal;
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
import com.techelevator.tenmo.models.User;

public class UserService
{
	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	private AuthenticatedUser user = null;

	public UserService(String url)
	{
		this.BASE_URL = url;
	}
	
	public void setUser(AuthenticatedUser user)
	{
		this.user = user;
	}
	
	public List<User> getAllUsers()
	{
		
		List<User> users = null;
		try
		{
		User[] userArray = restTemplate.exchange(BASE_URL, HttpMethod.GET,
				makeAuthEntity(), User[].class).getBody();
		users = Arrays.asList(userArray);
		}
		catch (RestClientResponseException e)
		{
			
		}
		return users;
	}

	public User getUserByName(String username)
	{
		User user = null;
		List<User> users = getAllUsers();
		for (User userSearch : users)
		{
			if(userSearch.getUsername().equalsIgnoreCase(username))
			{
				user = userSearch;
				break;
			}
		}
		return user;	
	}

	
	public BigDecimal getBalance()
	{
		BigDecimal balance = BigDecimal.valueOf(0);
		try
		{
		balance = restTemplate.exchange(BASE_URL + "/balance", HttpMethod.GET,
				makeAuthEntity(), BigDecimal.class).getBody();
		}
		catch (RestClientResponseException e)
		{
			
		}
		return balance;
	}
	
	public BigDecimal getBalance(int userId)
	{
		BigDecimal balance = BigDecimal.valueOf(0);
		try
		{
		balance = restTemplate.exchange(BASE_URL + "/balance/" + userId, HttpMethod.GET,
				makeAuthEntity(), BigDecimal.class).getBody();
		}
		catch (RestClientResponseException e)
		{
			
		}
		return balance;
	}

	public List<Transfer> getTransfersByUserId(int userId)
	{
		List<Transfer> transfers = null;
		if(this.user !=null)
		{
			try
			{
			Transfer[] transferArray = restTemplate.exchange(BASE_URL + "/transfers/" + userId, HttpMethod.GET,
					makeAuthEntity(), Transfer[].class).getBody();
			transfers = Arrays.asList(transferArray);
			}
			catch (RestClientResponseException e)
			{
				
			}
		}
		return transfers;
	}
	
	public String[] getAllUserNames()
	{
		List<User> users = getAllUsers();
		String[] usernames = new String[users.size()];
		for(int i=0; i < users.size(); i++)
		{
			usernames[i] = users.get(i).getUsername();
		}
		return usernames;
	}
	
	
	private HttpEntity makeAuthEntity()
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(this.user.getToken());
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
	
	private HttpEntity<User> makeUserEntity(User user)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(this.user.getToken());
		HttpEntity<User> entity = new HttpEntity<>(user, headers);
		return entity;
	}

}
