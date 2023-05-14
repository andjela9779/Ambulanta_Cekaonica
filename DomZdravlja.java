import java.util.*;
public class DomZdravlja {
    private static final int NOVI_PACIJENT = 1;
    private static final int ZAVRŠEN_PREGLED = 2;
    private static final int LISTA_PACIJENATA = 3;
    private static final int LISTA_DOKTORA = 4;
    private static final int LISTA_PREGLEDA = 5;
    private static final int KRAJ_RADA = 6;
    private String imeDZ;
    private ArrayList<Doktor> listaDoktora;
    private ArrayList<Pacijent> listaPacijenata;
    private ArrayList<Pregled> listaPregleda;

    // Konstruktor

    public DomZdravlja (String imeDZ) {
        this.imeDZ = imeDZ;
        listaDoktora = new ArrayList<Doktor>();
        listaPacijenata = new ArrayList<Pacijent>();
        listaPregleda = new ArrayList<Pregled>();
    }
    public void otvoriRecepciju(Scanner t) {
        String ime, id, spec;
        String još;
        System.out.println(" Dom zdravlja \"" + imeDZ + "\"");
        System.out.println("\nUnesite listu doktora u smjeni:");
        System.out.println();
        do {
            System.out.print(" Ime doktora: ");
            ime = t.nextLine(); // skener - sprema se unos korisnika
            System.out.print(" ID doktora : ");
            id = t.nextLine();
            System.out.print("Specijalnost doktora: ");
            spec = t.nextLine();
            Doktor dok = new Doktor(ime, id, spec);
            listaDoktora.add(dok);
            System.out.print("\nSljedeći doktor (d/n)? ");
            još = t.nextLine();
        } while (još.toUpperCase().equals("D"));    // dok korisnik odabire d odnosno da
    }
    public void otvoriČekaonicu(Scanner t) {
        int opcija;
        do {
            prikažiOpcije();
            opcija = t.nextInt();
            t.nextLine(); // preskoči znak za novi red
            switch (opcija) {
                case NOVI_PACIJENT:
                    dodajPacijenta(t);
                    break;
                case ZAVRŠEN_PREGLED:
                    završiPregled(t);
                    break;
                case LISTA_PACIJENATA:
                    prikažiPacijente();
                    break;
                case LISTA_DOKTORA:
                    prikažiDoktore();
                    break;
                case LISTA_PREGLEDA:
                    prikažiPreglede();
                    break;
                case KRAJ_RADA:
                    break;
                default:
                    System.out.println("\nOdabrali ste pogrešnu opciju!");
            }
        } while (opcija != KRAJ_RADA);
    }
    private void prikažiOpcije() {
// Prikazivanje menija na ekranu
        System.out.println();
        System.out.println("Opcije menija su:");
        System.out.println(" 1. Novi pacijent u čekaonici");
        System.out.println(" 2. Završen pregled pacijenta");
        System.out.println(" 3. Lista pacijenata u čekaonici");
        System.out.println(" 4. Lista slobodnih doktora");
        System.out.println(" 5. Lista trenutnih pregleda");
        System.out.println(" 6. Kraj rada");
        System.out.print("Unesite broj opcije: ");
    }
    private void prikažiDoktore() {
        int n = listaDoktora.size();
        if (n == 0) {          // ako nema elemenata u array-u doktori
            System.out.println();
            System.out.println("\nNema slobodnih doktora...");
        }
        else {
            System.out.println("\nSlobodni doktori:");
            for (int i = 0; i < n; i++)
                listaDoktora.get(i).prikaži();
        }
    }
    private void prikažiPacijente() {
        int n = listaPacijenata.size();
        if (n == 0) {
            System.out.println();
            System.out.println("\nNema pacijenata koji čekaju...");
        }
        else {
            System.out.println("\nPacijenti koji čekaju:");
            for (int i = 0; i < n; i++)
                listaPacijenata.get(i).prikaži();
        }
    }
    private void prikažiPreglede() {
        int n = listaPregleda.size();
        if (n == 0) {
            System.out.println();
            System.out.println("\nNema pregleda koji su u tijeku...");
        }
        else {
            System.out.println("\nPregledi koji su u tijeku:");
            for (int i = 0; i < n; i++)
                listaPregleda.get(i).prikaži();
        }
    }
    private void dodajPacijenta(Scanner t) {
        String ime, jmbg, simptom, med_oblast_bolesti;
        System.out.println();
        System.out.print(" Ime pacijenta: ");
        ime = t.nextLine();
        System.out.print(" JMBG pacijenta: ");
        jmbg = t.nextLine();
        System.out.print(" Simptomi bolesti: ");
        simptom = t.nextLine();
        System.out.print("Med. oblast bolesti: ");
        med_oblast_bolesti = t.nextLine();
        Pacijent pac = new Pacijent(ime, jmbg, simptom, med_oblast_bolesti);  // stvaramo novu instancu klase pacijent i dodajemo svaku instancu odnosno objekt u listu listaPacijenata
        listaPacijenata.add(pac);
        provjeriČekaonicu();
    }
    private void završiPregled(Scanner t) {
        String id;
        System.out.println();
        System.out.print("\nID doktora kod koga je završen pregled: ");
        id = t.nextLine();
        // Traženje doktora u listi pregleda
        for (int i = 0; i < listaPregleda.size(); i++) {
            Pregled preg = listaPregleda.get(i);
            Doktor dok = preg.getDok();
            if (id.equals(dok.getID())) {
                Pacijent pac = preg.getPac();
                listaPacijenata.remove(pac);
                listaDoktora.add(dok);
                System.out.println("\nDoktor " + id + " je slobodan.");
                listaPregleda.remove(preg);
                provjeriČekaonicu();
                return;
            }
        }
        System.out.print("\nGreška: doktor " + id + " nije pronađen u listi pregleda.");
    }
    private void provjeriČekaonicu() {         // provjeriti je li ok
        // Provjera pacijenata koji čekaju
        for (int i = 0; i < listaPacijenata.size(); i++) {
            Pacijent pac = listaPacijenata.get(i);
            for (int j = 0; j < listaDoktora.size(); j++) {
                Doktor dok = listaDoktora.get(j);
                if (pac.getMob().equals(dok.getSpec())) {
                    Pregled preg = new Pregled(dok, pac);
                    listaPregleda.add(preg);
                    listaPacijenata.remove(pac);
                    i--; // pacijent uklonjen iz liste pacijenata
                    listaDoktora.remove(dok);
                    System.out.println("\nNovi pregled: ");
                    preg.prikaži();
                    break;
                }
            }
        }
    }
    // "Klijentska strana" za testiranje

    public static void main(String[] args) {
        Scanner unos = new Scanner(System.in);
        DomZdravlja dz = new DomZdravlja("Mostar");
        dz.otvoriRecepciju(unos);
        dz.otvoriČekaonicu(unos);
    }
}
class Pacijent {
    private String ime;
    private String jmbg;
    private String simptom;
    private String med_oblast_bolesti; //
    public Pacijent(String ime, String jmbg, String simptom, String med_oblast_bolesti) {
        this.ime = ime;
        this.jmbg = jmbg;
        this.simptom = simptom;
        this.med_oblast_bolesti = med_oblast_bolesti;
    }
    public String getIme () {
        return ime;
    }
    public String getMob () {  // med. oblast bolesti
        return med_oblast_bolesti;
    }
    public void prikaži () {
        System.out.printf("\n--------------------------------------\n");
        System.out.printf(" Ime pacijenta: %s\n", ime);
        System.out.printf(" JMBG pacijenta: %s\n", jmbg);
        System.out.printf(" Simptomi bolesti: %s\n", simptom);
        System.out.printf("Med. oblast bolesti: %s\n", med_oblast_bolesti);
    }
}
class Doktor {
    private String ime;
    private String id;
    private String spec;
    public Doktor (String ime, String id, String spec) {
        this.ime = ime;
        this.id = id;
        this.spec = spec;
    }
    public String getIme () {
        return ime;
    }
    public String getID () {
        return id;
    }
    public String getSpec () {
        return spec;
    }
    public void prikaži () {
        System.out.printf("\n--------------------------------------\n");
        System.out.printf(" Ime doktora: %s\n", ime);
        System.out.printf(" ID doktora: %s\n", id);
        System.out.printf("Specijalnost doktora: %s\n", spec);
    }
}
class Pregled {
    private Doktor dok;
    private Pacijent pac;
    public Pregled (Doktor dok, Pacijent pac) {
        this.dok = dok;
        this.pac = pac;
    }
    public Doktor getDok () {
        return dok;
    }
    public Pacijent getPac () {
        return pac;
    }
    public void prikaži () {
        dok.prikaži();
        pac.prikaži();
    }
}



