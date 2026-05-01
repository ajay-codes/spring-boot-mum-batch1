package com.example;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoyGirlService {

    private final BoyRepository boyRepository;
    private final GirlRepository girlRepository;

    @Transactional
    public void doSomething() {
        // Boy boy1 = boyRepository.findById(1L).get();
        // System.out.println(boy1.getName());
        // System.out.println("----------------");
        // System.out.println(boy1.getGirlfriend().getName());

        Girl girl1 = girlRepository.findById(1L).get();
        System.out.println(girl1.getName());
        System.out.println("----------------");
        System.out.println(girl1.getBoyfriend().getName());

    }

}
