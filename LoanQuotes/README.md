# Quote Loans #

## Problem description ##
Develop a rate calculation system to allow prospective borrowers to obtain a quote from our pool of lenders for 36 month loans. This system will take the form of a command-line application.

You will be provided with a file containing a list of all the offers being made by the lenders within the system in CSV format.

You should strive to provide as low a rate to the borrower as possible. You should also provide the borrower with the details of the monthly repayment amount and the total repayment amount.

Repayment amounts should be displayed to 2 decimal places and the rate of the loan should be displayed to one decimal place.

Borrowers should be able to request a loan of any £100 increment between £1000 and £15000
inclusive. 

If the market does not have sufficient offers from lenders to satisfy the loan then the
system should inform the borrower that it is not possible to provide a quote at that time.

## Feedback received from delivered solution ##

1. Good separation of concerns
2. Missing CSV file decoupling. 
3. Package naming does not hint what they are for (quotation and internal) 
4. Making methods public just for testing reasons It's a bad practice. 
5. The logic reading the CSV file is not decoupled, so It's duplicated in a test where that logic is tested. The logic should be tested on the production class, in case it is changed. 
6. Lender constructor accepting an array of strings and parsing. Not good. 
7. Quote has all the logic in the constructor.