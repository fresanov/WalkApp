# WalkApp
Aplikacija sluzi za mjerenje prevaljene udaljenosti tijekom hoda, istovremeno mjeri i broj koraka koji korisnik napravi. 
Prilikom prvog pritiska na dugme "start" trazi se od korisnika da unese tjelesnu tezinu koja se koristi prilikom izracuna
potrosenih kalorija. Potom korisnik mora ponovno pritisnuti "start" kako bi mjerenje zapocelo, ovo se dogada samo prvi put
buduci da se podatak o tezini sprema u "shared preference". Nakon pritiska na start pokrece se Service koji biljezi koordinate,
a tekst dugmeta se mijenja u Stop. Service koordinate biljezi svakih deset metara u posebnu datoteku na uredaju gdje upisuje
kordinate u svaku liniju posebno, s time da je prva linija jedinstveni identifikator svakog pojedinacnog mjerenja. Pritiskom na
dugme Stop mjerenje staje, Service se gasi, s time da neposredno prije gasenja unose se u bazu podataka rezultati mjerenja.
Datum mjerenja, prevaljena udaljenost, napravljeni koraci i potrosene kalorije se skupa sa pocetnim i krajnjim koordinatama 
pohranjuju u SQLite bazu podataka. Pritiskom na tipku History izlistavaju se podaci o svakom mjerenju tablicno. Pritiskom na 
pojedinacni redak otvara se karta na kojoj je prikazana prijedena putanja s pocetnim i krajnjim markerom. Pritiskom na tipku 
Delete History brise se tablica iz baze podataka skupa sa tekstualnom datotekom sa izmjerenim koordinatama. Buduci da se mjerenje
dogada u pozadinskom servisu, gasenje ekrana nece utjecati na rad aplikacije. Pritiskom na tipku Modify weight korisnik
promjeniti podatak o svojoj tjelesnoj masi koji se koristi kod izracuna potrosenih kalorija. Pritiskom na tipku Get Weather od 
korisnika se trazi da unese ime lokacije na kojoj se nalazi. Taj podatak se koristi za dohvacanje vremenske prognoze za tu 
lokaciju za narednih 15 sati svaka 3 sata. Za dohvacanje vremenske prognoze aplikacija koristi openweathermap API. 
Aplikacija je uspjesno testirana na uredaju Samsung S5 neo sa operativnim sustavom Android Lollipop(API 21).
