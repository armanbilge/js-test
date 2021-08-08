# js-test - serverless AWS lambda [![Build Status](https://travis-ci.com/ChristopherDavenport/js-test.svg?branch=master)](https://travis-ci.com/ChristopherDavenport/js-test) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/js-test_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/js-test_2.12) ![Code of Consuct](https://img.shields.io/badge/Code%20of%20Conduct-Scala-blue.svg)

1. Install the [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html).
2. Package the lambda with `sbt packageLambda`
3. Deploy the lambda locally with `sam local start-api`
4. Give it a try with `curl -v http://127.0.0.1:3000/hello/foo`
