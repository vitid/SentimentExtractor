# SentimentExtractor
Aspect-Sentiment Extractor module(as part of CSCI-544 final project)

Consisted of Coreference Resolution Module and Aspect-Sentiment Linking Module. Both modules can be run seperately.

# Build
Firstly, install stanford-corenlp and its model into your local maven repository(maven repository for version 3.7.0 is not available at the time of this writing).

Download [CoreNLP 3.7.0(beta)](http://stanfordnlp.github.io/CoreNLP/), extract, and run

```
mvn install:install-file -Dfile=stanford-corenlp-3.7.0.jar -DgroupId=edu.stanford.nlp -DartifactId=stanford-corenlp -Dversion=3.7.0 -Dpackaging=jar

mvn install:install-file -Dfile=stanford-corenlp-3.7.0-models.jar -DgroupId=edu.stanford.nlp -DartifactId=stanford-corenlp -Dversion=3.7.0 -Dclassifier=models -Dpackaging=jar
```

To build, run

```
mvn clean compile assembly:single install
```

File named `SentimentExtractor-1.0-SNAPSHOT-jar-with-dependencies.jar` should be installed into your local repository.

Noted: Testing required CoreNLP to load models for both modules, which requires ~4-6 GB memory. If your machine doesn't have that much memory, simply turn the Testing phase off by setting `<skipTests>true</skipTests>` in `pom.xml`

# Run
Put `SentimentExtractor-1.0-SNAPSHOT-jar-with-dependencies.jar` and script files in [script folder](script/) into the same directory.


To run Coreference Resolution Module:
```
runCorefModule.sh {input_dir} {output_dir}
```


To run Aspect-Sentiment Linking Module:
```
runAspectSentimentLinkingModule.sh {input_dir} {output_dir}
```


Log data is printed out in app.log
