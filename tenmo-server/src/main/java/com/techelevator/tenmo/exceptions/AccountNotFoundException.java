package com.techelevator.tenmo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.NOT_FOUND, reason = "Account Not Found.")
public class AccountNotFoundException extends RuntimeException
{

}
