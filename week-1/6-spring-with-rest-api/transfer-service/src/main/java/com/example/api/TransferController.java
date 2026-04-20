package com.example.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.TransferRequest;
import com.example.dto.TransferResponse;
import com.example.service.TransferService;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = { "application/json" }, produces = { "application/json" })
    public ResponseEntity<?> processTransfer(@RequestBody TransferRequest transferRequest) {
        // authentication and authorization logic can be added here
        // transfer processing logic can be added here
        transferService.transfer(
                transferRequest.getFromAccountNumber(),
                transferRequest.getToAccountNumber(),
                transferRequest.getAmount());
        TransferResponse model = new TransferResponse("success", "Transfer completed successfully");
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<TransferResponse> handleRuntimeException(RuntimeException ex) {
        TransferResponse model = new TransferResponse("failure", ex.getMessage());
        return new ResponseEntity<>(model, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
