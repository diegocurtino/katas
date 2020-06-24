## Build the image ##
1. Go to the directory where Dockerfile is.
2. Run docker build . -t loan-quotes-image.

## Run a container ##
1. Run docker run -t --name loan-quotes-container loan-quotes-image lenders.csv <amount-to-loan>

This runs the application packed in the container with 2 parameters. The first one, the lenders file that
is copied into the image and the second one, the amount to loan. 