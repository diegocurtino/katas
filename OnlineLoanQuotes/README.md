# Online Quote Loans #

This is a RESTFul version of the CLI Quote Loans application. Both were developed using Spring Boot. The functionality 
is the same and the entities are the same. 

# Functionality Description ##

The application is a rate calculation system that allow prospective borrowers to obtain a quote from a specified (more 
on this below)pool of lenders for a 36- month loans.

The list of all the offers being made by the lenders is provided in a CSV file.

The application should strive to provide as low a rate to the borrower as possible. The application should also provide
the borrower with the details of the monthly repayment amount and the total repayment amount.

Repayment amounts should be displayed to 2 decimal places and the rate of the loan should be displayed to one decimal place.

Borrowers should be able to request a loan of any £100 increment between £1000 and £15000 inclusive.

If the market does not have sufficient offers from lenders to satisfy the loan then the system should inform the borrower 
that it is not possible to provide a quote at that time.

### OpenAPI / Swagger links: ###
- http://localhost:8080/v3/api-docs
- http://localhost:8080/swagger-ui.html

### Reading material:
- https://blog.knoldus.com/spring-webflux-how-to-test-your-controllers-using-webtestclient/
- https://howtodoinjava.com/spring-boot2/testing/webfluxtest-with-webtestclient/