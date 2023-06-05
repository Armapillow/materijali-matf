# Komande i resursi

Za testiranje TCP-a:
* Server: `nc -l <port>`
* Klijent: `nc <host> <port>`

Za testiranje UDP-a:
* Server: `nc -lu <port>`
* Klijent: `nc -u <host> <port>`

Za testiranje Non-blocking I/O:
* Server: `nc -l <port>`
* Klijent: `nc <host> <port>`

Provera za protocol handlers:
* Mock server: `echo "something" | nc -l <port>`
* Pokrenuti server iz prethodnog zadatka (jer su povezani zadaci)

Provera da li su otvoreni port-ovi:
* `lsof -i:<port>`
* `nc -zuv <host> <port>`
* `netstat -tulpn`

---

Weather app za testiranje 1. zadatka, jan2 2022:
[link](https://github.com/Adedoyin-Emmanuel/react-weather-app)
