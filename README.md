Simple uni project for "Research Methods" (CS4040, University of Aberdeen, 2018)

I used this to create some randomised Datasets and evaluate reading times on some different partitioning schemas. 

simply clone into your eclipse workspace and import from eclipse as maven project (will not work without pom.xml)

Java Compiler: 1.8
requires Apache-Cassandra tested on 3.11.3 (use JDK/JRE 1.8 or lower to start this up first): http://cassandra.apache.org/
Copyright: University of Aberdeen, I guess
Rights: probably reserved by the above, but as an educational institution (and this being part of course work) it is free to use for educational purposes. However, careful if you submit any of this, as without properly appropriating other people's work you are plagiarising!

launch from Launcher.main(String[]) with following arguments:
	to create Dataset and write into Cassandra: WRITE {IDEAL,NORMAL,WORST} <number of entries to create>
	to use existing Dataset and create readings: READ

