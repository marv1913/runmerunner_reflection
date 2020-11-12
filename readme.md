## "mini framework" - runmerunner
This command line application uses reflection to show all declared methods of a given Java class. Additionally it shows all methods which are annotated with the "RunMe.java" Annotation from source directory. These annotated methods are executed by the runmerunner.

## Deployment

 1. clone repository 
 2. optional: add your own Java classes and add RunMe annotation to your methods
 3. run mvn package
 4. cd to target directory (created by "mvn package" command) 
 5. start application with "java -jar  runmerunner-marvEn-jar-with-dependencies.jar"

## Usage
java -jar  runmerunner-marvEn-jar-with-dependencies.jar [java class name]
e.g.:
java -jar  runmerunner-marvEn-jar-with-dependencies.jar java.lang.String
