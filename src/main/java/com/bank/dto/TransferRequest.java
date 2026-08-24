package com.bank.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {

	  @NotBlank(message = "Sender Account Number is Required")
	  private String fromAccount;

	@NotBlank(message = "Reciever Account Number is Required")
	private String toAccount;

	   @NotBlank(message = "Valid ammount is required")
	    private Double amount;
}
