#Online Quote Loans

This is a RESTFul version of the Quote Loans application developed in Spring boot. The functionality is the same and the
entities are the same. 

The intention is to develop the application so that it exposes one REST endpoint where the amount requested as a loan is
sent as a request parameter.

HTTP Error notifications such as bad request (400) and element not found (404) are handled with a CustomErrorController
that overrides Springboot's default whitelabel page. 