package com.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.repository.TransferHistoryRepository;

@Controller
@RequestMapping("/transfer-history")
public class TransferHistoryController {

    private final TransferHistoryRepository transferHistoryRepository;

    public TransferHistoryController(TransferHistoryRepository transferHistoryRepository) {
        this.transferHistoryRepository = transferHistoryRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getTransferHistory() {
        ModelAndView modelAndView = new ModelAndView("transfer-history");
        modelAndView.addObject("transferHistory", transferHistoryRepository.findAll());
        return modelAndView;
    }
}
