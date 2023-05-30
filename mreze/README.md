# Komande i resursi

Za testiranje UDP-a:
* Server: `nc -lu <port>`
* Klijent: `nc -u <host> <port>`

Provera da li su otvoreni port-ovi:
* `lsof -i:<port>`
* `nc -zuv <host> <port>`
* `netstat -tulpn`

---

Weather app za testiranje 1. zadatka, jan2 2022:
[link](https://github.com/Adedoyin-Emmanuel/react-weather-app)
