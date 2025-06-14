AxoRTP to plugin do Minecrafta umożliwiający losową teleportację graczy na bezpieczne lokalizacje z intuicyjnym GUI, indywidualnymi cooldownami i integracją z PlaceholderAPI.
Funkcjonalności

Losowa teleportacja: Trzy opcje RTP (#1, #2, #3) z różnymi odległościami (500, 1000, 1500 kratek).

Intuicyjne GUI: Wybór opcji RTP za pomocą interfejsu graficznego (/rtp).

Cooldowny: Konfigurowalne czasy oczekiwania dla każdej opcji RTP (domyślnie 300, 600, 900 sekund).

Bezpieczne lokalizacje: Teleportacja tylko na powierzchnię, na bezpieczne bloki (np. trawa, kamień).

Integracja z PlaceholderAPI: Placeholdery: %axortp_rtp1_cooldown%, %axortp_rtp2_cooldown%, %axortp_rtp3_cooldown%, %axortp_cooldown%.

Wymagania

Serwer: Minecraft 1.20.4 (Spigot/Paper).
Wtyczki: PlaceholderAPI (wymagany).
Java: Wersja 8 lub nowsza.

Instalacja

Pobierz plik AxoRTP-1.0-SNAPSHOT.jar.
Umieść go w folderze plugins/ na serwerze..
Uruchom serwer, aby wygenerować plik config.yml.

Komendy i permisje

/rtp: Otwiera GUI teleportacji (dostępne dla wszystkich graczy).
/axortp reload: Przeładowuje konfigurację (permisja: axortp.reload, domyślnie dla operatorów).
Permisje:
axortp.rtp2: Dostęp do RTP #2 (domyślnie dla operatorów).
axortp.rtp3: Dostęp do RTP #3 (domyślnie dla operatorów).
axortp.reload: Przeładowanie konfiguracji (domyślnie dla operatorów).



Konfiguracja
Plik config.yml pozwala na dostosowanie:

Odległości i cooldownów dla RTP #1, #2, #3.
Materiałów i opisów w GUI.
Bezpiecznych i niebezpiecznych bloków.
Komunikatów (np. tytuły, podtytuły, cooldowny).

Autor

AxoPL777

Wsparcie
W przypadku problemów skontaktuj się z autorem lub dołącz na discorda (dc.axostudio.pl).
