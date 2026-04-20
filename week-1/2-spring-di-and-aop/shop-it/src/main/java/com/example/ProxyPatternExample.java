package com.example;

/**
 * Standalone Proxy Pattern demo (no Spring).
 * Shows how cross-cutting concerns (logging, auth) can be added via a proxy
 * without modifying the real service. This is the manual approach that Spring AOP automates.
 */

class Logger{
    public void logInfo(String message){
        System.out.println("INFO: " + message);
    }
    public void logError(String message){
        System.out.println("ERROR: " + message);
    }
}

class Auth{
    public void isAuthenticated(){
        System.out.println("User is authenticated");
    }
}

interface Training{
    void getSpringBootTraining();
    void getSqlTraining();
}

class TrainingService implements Training{
    public void getSpringBootTraining(){
        System.out.println(">>>>Spring Boot Training");
    }
    public void getSqlTraining(){
        System.out.println(">>>>SQL Training");
    }
}

class TrainingServiceProxy implements Training{
    TrainingService trainingService= new TrainingService();
    Logger logger = new Logger();
    Auth auth = new Auth();
    public void getSpringBootTraining(){
        auth.isAuthenticated();
        logger.logInfo("Spring Boot Training accessed");
        trainingService.getSpringBootTraining();
    }
    public void getSqlTraining(){
        auth.isAuthenticated();
        logger.logInfo("SQL Training accessed");
        trainingService.getSqlTraining();
    }
}

public class ProxyPatternExample {

    public static void main(String[] args) {

        Training training = new TrainingServiceProxy();
        training.getSpringBootTraining();
        training.getSqlTraining();

    }
    
}
