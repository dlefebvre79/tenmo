package com.techelevator.tenmo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST, reason = "Insufficent Funds to Initiate Transfer.")
public class InsufficientFundsException extends RuntimeException
{

}
