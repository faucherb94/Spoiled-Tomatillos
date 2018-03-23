#!/bin/bash

wars=($(ls app))

# Run the most recent file. It'll have the largest build number.
java -jar /home/ec2-user/app/${wars[-1]} > app.out 2>&1 &

