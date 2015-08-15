# Summary
- contains the basic use of spring bean life cycle annotations :  @PostConstruct, @PreDestroy
- contains the basic use of CompletionService with ExecutorService
  - in a method annotated with @PostConstruct
    - creates CompletionService with an ExecutorService
  - in a method annotated with @PreDestroy
    - makes a shutdown call to the ExecutorService made in the method annotated with CompletionService
