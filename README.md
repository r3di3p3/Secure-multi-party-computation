# Secure-multi-party-computation
Secure multi-party computation

Test to create Keys
java -jar smpc.jar -e -st T -s SecretText -nk 4 -km AUTO -om 1 -m 1000 -w outputfloder -ch
java -jar smpc.jar -e -st F -s secret.txt -nk 4 -km AUTO -om 1 -m 1000 -w outputfloder -ch
java -jar smpc.jar -e -st F -s secret.txt -nk 4 -km USER -om 1 -m 1000  -V 450 459 465 425 -w outputfloder -ch
Test to get secret // Values 450 459 465 425 change 
java -jar dist\ProjectSMPC.jar -d -w test2.txt -xval 450 459 465 425 -k test\part0.p test\part1.p test\part2.p test\part3.p
