# CardCreate2

'CardCreate2' is a Java application that generates images representing playing 
cards.

## Overview

'CardCreate2' generates .png images representing a pack of playing cards from 
existing images. It is very configurable, allowing for playing cards of 
greatly varying styles to be created.

To use 'CardCreate2' you will need a Java Development Kit and Maven installed. 

## Cloning and Building

The code has been structured as a standard Maven project which means you need 
to have Maven and a JDK installed. A quick web search will help, but if not 
https://maven.apache.org/install.html should guide you through the install.

The following command clones 'CardCreate2':

    git clone https://github.com/PhilLockett/CardCreate2.git

## Setting up the 'CardCreate2' environment

'CardCreate2' works in an environment which provides the component images 
needed to compose the playing card images. BEFORE running 'CardCreate2' it is 
recommended that the environment is setup first. The GitHub repository 
contains the file 'CardCreate2/CardWork.tar.gz' which provides this 
environment. It is recommended that this environment is set up outside of the 
'CardCreate2' directory. The environment can be set up in the parent 
directory of 'CardCreate2' with the following commands:

    cp CardCreate2/CardWork.tar.gz .
    tar zxf CardWork.tar.gz
    cd CardWork/
    ./setup.sh

## Running

'CardCreate2' can be launched from the 'CardCreate2' direcotry using the maven 
command:

    mvn clean javafx:run

When running for the first time, 'CardCreate2' requires that you to select the 
environment you setup. Browse to the CardWork directory created above from the 
CardCreate2/CardWork.tar.gz file. Once setup you will not be prompted again, 
however, you can click on the "Browse..." button at any time to select another 
instance of the environment.

Warning: the standard `mvn clean` command will remove all generated files, 
including any environment file paths previously set up.

## Further reading

The document 'Card Generator User Guide.pdf' describes the installation, the 
environment set up and 'CardCreate2' usage with many examples.

## Additional packages

Additional packages are currently unavailable.

## Points of interest

This code has the following points of interest:

  * CardCreate2 is the JavaFX version of CardCreate.
  * CardCreate2 is a maven project that uses JavaFX.
  * The user GUI was developed using SceneBuilder utilizing FXML and CSS.
  * CardCreate: https://github.com/PhilLockett/CardCreate.git

  