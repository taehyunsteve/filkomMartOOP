import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Barang {
    private final String nama;
    private final int jumlah;
    private final double hargaSatuan;

    public Barang(String nama, int jumlah, double hargaSatuan) {
        this.nama = nama;
        this.jumlah = jumlah;
        this.hargaSatuan = hargaSatuan;
    }

    public String getNama() { return nama; }
    public int getJumlah() { return jumlah; }
    public double getHargaSatuan() { return hargaSatuan; }
    public double getTotalHarga() { return jumlah * hargaSatuan; }
}

class FilkomMart {
    private final List<Barang> daftarBarang = new ArrayList<>();
    private String namaKasir = "KASIR";
    private final String namaToko = "FILKOM MART UB";
    private final String noStruk = "FM-" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
    private final ZonedDateTime waktuTransaksi = ZonedDateTime.now(ZoneId.of("Asia/Jakarta"));

    public void setNamaKasir(String kasir) {
        if (kasir != null && !kasir.isBlank()) this.namaKasir = kasir.trim();
    }

    public void tambahBarang(Barang b) { daftarBarang.add(b); }

    public void cetakStruk(double uangBayar) {
        double totalBelanja = 0;
        for (Barang b : daftarBarang) totalBelanja += b.getTotalHarga();

        double diskon = hitungDiskon(totalBelanja);
        double totalAkhir = totalBelanja - diskon;
        double kembalian = uangBayar - totalAkhir;

        System.out.println();
        garis('=');
        System.out.printf("  %s%n", namaToko);
        garis('=');
        System.out.printf("No Struk : %s%n", noStruk);
        System.out.printf("Tanggal  : %s%n",
                waktuTransaksi.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy HH:mm:ss", new Locale("id","ID"))));
        System.out.printf("Kasir    : %s%n", namaKasir);
        garis('-');

        System.out.printf("%-20s %5s %15s %15s%n", "Nama Barang", "Qty", "Harga", "Total");
        garis('-');

        for (Barang b : daftarBarang) {
            System.out.printf("%-20s %5d %15s %15s%n",
                    b.getNama(),
                    b.getJumlah(),
                    rupiah(b.getHargaSatuan()),
                    rupiah(b.getTotalHarga()));
        }

        garis('-');
        System.out.printf("%-27s %13s%n", "Total Belanja", rupiah(totalBelanja));
        System.out.printf("%-27s %13s%n", "Diskon", rupiah(diskon));
        System.out.printf("%-27s %13s%n", "Total Akhir", rupiah(totalAkhir));
        System.out.printf("%-27s %13s%n", "Uang Bayar", rupiah(uangBayar));
        System.out.printf("%-27s %13s%n", "Kembalian", rupiah(kembalian));
        garis('=');
        System.out.println("Terima kasih sudah belanja! 😊");
        garis('=');
        System.out.println();
    }

    private double hitungDiskon(double total) {
        if (total > 500_000) return total * 0.20;
        if (total > 100_000) return total * 0.10;
        if (total > 50_000)  return total * 0.05;
        return 0;
    }

    private static String rupiah(double nilai) {
        Locale id = new Locale("id","ID");
        var nf = java.text.NumberFormat.getCurrencyInstance(id);
        nf.setMaximumFractionDigits(0);
        nf.setMinimumFractionDigits(0);
        return nf.format(Math.round(nilai));
    }

    private static void garis(char c) {
        for (int i = 0; i < 48; i++) System.out.print(c);
        System.out.println();
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        FilkomMart toko = new FilkomMart();

        System.out.print("Nama kasir: ");
        String kasir = in.nextLine();
        toko.setNamaKasir(kasir);

        System.out.print("Masukkan jumlah barang yang dibeli: ");
        int n = readInt(in);

        for (int i = 1; i <= n; i++) {
            System.out.println("\nBarang ke-" + i);
            System.out.print("Nama barang: ");
            String nama = in.nextLine();

            System.out.print("Jumlah satuan: ");
            int qty = readInt(in);

            System.out.print("Harga satuan: ");
            double harga = readDouble(in);

            toko.tambahBarang(new Barang(nama, qty, harga));
        }

        System.out.print("\nMasukkan jumlah uang yang dibayarkan: ");
        double bayar = readDouble(in);

        toko.cetakStruk(bayar);
        in.close();
    }

    private static int readInt(Scanner in) {
        while (!in.hasNextInt()) {
            in.next(); System.out.print("Masukkan angka valid: ");
        }
        int val = in.nextInt();
        in.nextLine();
        return val;
    }

    private static double readDouble(Scanner in) {
        while (!in.hasNextDouble()) {
            in.next(); System.out.print("Masukkan angka valid: ");
        }
        double val = in.nextDouble();
        in.nextLine();
        return val;
    }
}
