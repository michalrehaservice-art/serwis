# Aplikacja „Serwis” na Androida (praca offline)

## Część 1 – serwer (Google Apps Script) – raz
1. Wklej nowy `Code.gs` (z tego pakietu) do projektu Apps Script i `Index.html` do pliku Index.
2. Uruchom funkcję `setup` (dochodzi kolumna „haslo” w arkuszu Uzytkownicy).
3. W arkuszu **Uzytkownicy** wpisz w kolumnie **haslo** hasło dla siebie i dla kolegi (zwykłym tekstem), potem w edytorze uruchom funkcję **zabezpieczHasla** – hasła zamienią się w nieodwracalny skrót.
4. Wdróż → **Nowe wdrożenie** → Aplikacja internetowa → *Wykonaj jako: Ja* → *Kto ma dostęp: Wszyscy* → Wdróż. Skopiuj adres kończący się na **/exec**.
   (Wersja w przeglądarce, logowana kontem Google, może zostać jako osobne, poprzednie wdrożenie.)

## Część 2 – zbudowanie APK (bez instalowania czegokolwiek na komputerze)
1. Załóż darmowe konto na github.com, utwórz **prywatne** repozytorium.
2. Wgraj do niego CAŁĄ zawartość tego folderu (razem z ukrytym folderem `.github`), tak by plik `build.gradle` był w głównym katalogu repozytorium.
3. Wejdź w zakładkę **Actions** → „Zbuduj aplikację (APK)” → **Run workflow**. Po 3–6 minutach w tym przebiegu pojawi się plik **serwis-apk** do pobrania (to jest `app-debug.apk`).
   (Alternatywa: otwórz folder w Android Studio → Build → Build APK.)

## Część 3 – instalacja na telefonie (Twoim i kolegi)
1. Prześlij plik APK na telefon (e-mail, Dysk Google).
2. Otwórz go – Android poprosi o zgodę „Zezwól na instalację z tego źródła”. Zaakceptuj. Play Protect może zapytać o skanowanie – wybierz „Zainstaluj mimo to”.
3. Uruchom „Serwis”. Przy pierwszym uruchomieniu wpisz: adres skryptu (/exec), login (e-mail z arkusza), hasło → „Zaloguj i pobierz dane”.

## Codzienna praca
- Z internetem: **🔄 Odbierz i wyślij** – wysyła Twoje zmiany i pobiera nowe dane.
- Bez internetu: wszystko jest w telefonie – przeglądanie, opis prac, koszty, nowe zlecenia, podpis klienta w protokole, zdjęcia.
- Podpisany protokół: PDF powstaje i trafia do folderu zlecenia oraz na service@rehaservice.pl dopiero przy synchronizacji.
- Konflikt (to samo pole zmienione przez dwie osoby): wygrywa nowsza zmiana z telefonu, a szczegóły (numer zlecenia, pole, obie wartości) przychodzą mailem na service@rehaservice.pl.
- Wymagają internetu: Karta PDF, Pliki zlecenia, Import z Excela/PDF, Wyniki badań, wysyłka maila do klienta. Wcześniej trzeba zsynchronizować zmiany.
