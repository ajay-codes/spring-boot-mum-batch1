package com.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.dto.TransferRequest;
import com.example.dto.TransferResponse;
import com.example.service.TransferService;

@Controller
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showTransferForm() {
        // authentication and authorization logic can be added here
        String viewName = "transfer-form"; // name of the view to render the transfer form
        return viewName;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView processTransfer(@ModelAttribute TransferRequest transferRequest) {
        // authentication and authorization logic can be added here
        // transfer processing logic can be added here
        transferService.transfer(transferRequest.getFromAccountNumber(), transferRequest.getToAccountNumber(),
                transferRequest.getAmount());
        TransferResponse model = new TransferResponse("success", "Transfer completed successfully");
        String viewName = "transfer-status"; // name of the view to render the transfer result
        ModelAndView modelAndView = new ModelAndView(viewName);
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException ex) {
        TransferResponse model = new TransferResponse("failure", ex.getMessage());
        String viewName = "transfer-status"; // name of the view to render the transfer result
        ModelAndView modelAndView = new ModelAndView(viewName);
        modelAndView.addObject("model", model);
        return modelAndView;
    }

}
