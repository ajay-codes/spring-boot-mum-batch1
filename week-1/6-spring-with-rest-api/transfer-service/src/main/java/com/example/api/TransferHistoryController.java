package com.example.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.repository.TransferHistoryRepository;

@RestController
@RequestMapping("/transfer-history")
public class TransferHistoryController {

    private final TransferHistoryRepository transferHistoryRepository;

    public TransferHistoryController(TransferHistoryRepository transferHistoryRepository) {
        this.transferHistoryRepository = transferHistoryRepository;
    }

    // @RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public ResponseEntity<?> getTransferHistory() {
        return new ResponseEntity<>(transferHistoryRepository.findAll(), HttpStatus.OK);
    }
}
