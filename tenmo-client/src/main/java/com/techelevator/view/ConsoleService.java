package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Scanner;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class ConsoleService
{

	private PrintWriter out;
	private Scanner in;

	public ConsoleService(InputStream input, OutputStream output)
	{
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options)
	{
		Object choice = null;
		while (choice == null)
		{
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options)
	{
		Object choice = null;
		String userInput = in.nextLine();
		try
		{
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length)
			{
				choice = options[selectedOption - 1];
			}
		}
		catch (NumberFormatException e)
		{
			// eat the exception, an error message will be displayed below since choice will
			// be null
		}
		if (choice == null)
		{
			out.println("\n*** " + userInput + " is not a valid option ***\n");
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options)
	{
		out.println();
		for (int i = 0; i < options.length; i++)
		{
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print("\nPlease choose an option >>> ");
		out.flush();
	}

	public String getUserInput(String prompt)
	{
		out.print(prompt + ": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt)
	{
		Integer result = null;
		do
		{
			out.print(prompt + ": ");
			out.flush();
			String userInput = in.nextLine();
			try
			{
				result = Integer.parseInt(userInput);
			}
			catch (NumberFormatException e)
			{
				out.println("\n*** " + userInput + " is not valid ***\n");
			}
		}
		while (result == null);
		return result;
	}
	
	public void displayBalance(BigDecimal balance)
	{
		out.println();
		out.println();
		out.print("Your current account balance is: $" + balance);
		out.println();
		out.flush();
	}
	
	public void displayUsers(List<User> users)
	{
		out.println();
		out.println();
		out.println(String.format("%50s", "").replace(' ', '-'));
		out.println("Users");
		out.println(String.format("%-12s%s", "ID", "Name"));
		out.println(String.format("%50s", "").replace(' ', '-'));
		
		for (User user : users)
		{
			out.println(String.format("%-12d%s", user.getId(), user.getUsername()));
		}
		
		out.println(String.format("%50s", "").replace(' ', '-'));
		out.println();
		out.flush();
	}
	
	public void displayTransfers(List<Transfer> transfers, String username)
	{
		out.println();
		out.println();
		out.println(String.format("%50s", "").replace(' ', '-'));
		out.println("Transfers");
		out.println(String.format("%-12s%-26s%12s", "ID", "From/To", "Amount "));
		out.println(String.format("%50s", "").replace(' ', '-'));
		
		for (Transfer transfer : transfers)
		{
			String from = transfer.getUserFrom();
			String to = transfer.getUserTo();
			
			String fromTo = "From:";
			String name = from;
			String amount = transfer.getAmount().toString();
			
			if(from.equalsIgnoreCase(username))
			{
				fromTo = "To:";
				name = to;
			}
			out.println(String.format("%-12d"	// Transfer ID
									+ "%-6s"	// From/To
									+ "%-20s"	// Name
									+ "%-2s"	// $
									+ "%10s"	// Amount
									, transfer.getId()
									, fromTo
									, name
									, "$ "
									, amount));
		}
		
		out.println(String.format("%50s", "").replace(' ', '-'));
		out.println();
		out.flush();
	
		
	}

	public void displayPendingTransfers(List<Transfer> transfers)
	{
		out.println();
		out.println();
		out.println(String.format("%50s", "").replace(' ', '-'));
		out.println("Pending Transfers");
		out.println(String.format("%-12s%-26s%12s", "ID", "To", "Amount "));
		out.println(String.format("%50s", "").replace(' ', '-'));
		
		for (Transfer transfer : transfers)
		{
			String name = transfer.getUserTo();
			String amount = transfer.getAmount().toString();
			
//			if(from.equalsIgnoreCase(username))
//			{
//				fromTo = "To:";
//				name = to;
//			}
			out.println(String.format("%-12d"	// Transfer ID
									+ "%-26s"	// Name
									+ "%-2s"	// $
									+ "%10s"	// Amount
									, transfer.getId()
									, name
									, "$ "
									, amount));
		}
		
		out.println(String.format("%50s", "").replace(' ', '-'));
		out.println();
		out.flush();
	
		
	}

	public void displayTransferDetails(Transfer transfer)
	{
		out.println();
		out.println();
		out.println(String.format("%50s", "").replace(' ', '-'));
		out.println("Transfer Details");
		out.println(String.format("%50s", "").replace(' ', '-'));
		
		out.println(String.format("%-12s%s", "ID:", transfer.getId()));
		out.println(String.format("%-12s%s", "From:", transfer.getUserFrom()));
		out.println(String.format("%-12s%s", "To:", transfer.getUserTo()));
		out.println(String.format("%-12s%s", "Type:", transfer.getTransferType()));
		out.println(String.format("%-12s%s", "Status:", transfer.getTransferStatus()));
		out.println(String.format("%-12s%s", "Amount:", "$" + transfer.getAmount()));
		
		out.println(String.format("%50s", "").replace(' ', '-'));
		out.println();
		out.flush();

	}

	public void displayInvalidId(int id)
	{
		out.println("\n*** ID " + id + " is not found ***\n");
	}
}
