# Secure-multi-party-computation
Secure multi-party computation
console application, allows to generate parts of keys using shamir's multi-part calculation. It is also used to generate secrecy from the parties.
# Example of use: 
Test to create Keys

```
java -jar smpc.jar -e -st T -s SecretText -nk 4 -km AUTO -om 1 -m 1000 -w outputfloder -ch
```

```
java -jar smpc.jar -e -st F -s secret.txt -nk 4 -km AUTO -om 1 -m 1000 -w outputfloder -ch
```

```
java -jar smpc.jar -e -st F -s secret.txt -nk 4 -km USER -om 1 -m 1000  -V 450 459 465 425 -w outputfloder -ch
```
# Help

    -e for encyption mode 

    -st Secret type : F for file, and T for text 

    -s secret file or text 

    -nk numbre of keys

    -km Keys Mode , to generate X value for each part, AUTO and USER

    -om output mode 1 or 2

    -m MAX X VALUE 

    -w output floder 

    -ch Check parts

    -V x values (using with Key Mode USER)

# Test

Test to get secret // Values 450 459 465 425 change 
```
java -jar dist\ProjectSMPC.jar -d -w test2.txt -xval 450 459 465 425 -k test\part0.p test\part1.p test\part2.p test\part3.p
```

## Help

-d decryption mode

-w output secret file 

-xavl X VAlues for each part

-k keys or parts

