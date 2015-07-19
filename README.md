JCrypt
======

JCrypt is an application that lets you encrypt/decrypt (cipher/decipher) files with a password through two ways :

- From a Graphic User Interface (GUI)
- From a Command-Line Interface (CLI)

It is based over the Blowfish algorithm provided by the Java Development Toolkit (JDK).

The Graphic User Interface
======

The GUI is designed to be as straightforward as possible, simply select the operation and options from the lower button bar then drop your files/folders in the specified area. You will be prompted for the password key and the output folder (if needed) during the process.

![](https://github.com/dwarfman78/jcrypt/blob/master/jcrypt-ihm/capture.png)

The Command-Line Interface
======

```
usage: jcrypt-cli
    --compress                                 Compress output
    --help                                     Display this help
    --input <INPUT DIR OR FILE>
    --key <KEY>
    --operation <C (cipher) or D (decipher)>
    --output <OUTPUT DIR>
    --verbose                                  Display debug infos to std
```

for example :

/usr/bin/java -jar /root/bin/jcrypt/jcrypt-cli.jar --input ${dumpfolder} --output ${dumpfolder}/encrypted --operation C --key myComplexPassword --compress --verbose >> /var/log/jcrypt/dumpscripts.log

Building
======

In order to build JCrypt, you will need Maven and a JDK8.

```
mvn clean install
```

When build is successful, resulting jars should be put in their respective target folders.

Note that both cli and gui jars need the base library (jcrypt-metier.jar) to work. Please make sure that this jar is added to the classpath.

Note that an .exe wrapper is available for Microsoft Windows here : https://github.com/dwarfman78/jcrypt/releases

