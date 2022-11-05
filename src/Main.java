import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException, IOException {
        int n = 1000;
//        int i;
//        int j;
//        Scanner scanner;
        int sum = 0;
        for (int i = 0; i < n; ++i) {
            Field f = new Field(10);
            f.GenerateRandomShips(new int[]{0, 0, 3});
            int c = 0;
            Bot bot = new Bot(f, 10);
            Probability g = new Probability(f);
            int s = 0;
            for (int k : g.quantity_ships) {
                s += k;
            }
            while (s > 0) {
                c += 1;
//                g.ShowStatusField();
//                System.out.println(c);
                bot.Fire();
//            int[] Max_pos=g.GetMaxProbabilityPos(n);
//            g.ShowProbabilityField();
//            System.out.println((Max_pos[0]+1)+" "+(Max_pos[1]+1));
//            scanner = new Scanner(System.in);
//            i = scanner.nextInt()-1;
//            scanner = new Scanner(System.in);
//            j = scanner.nextInt()-1;
//            f.Fire(new int[]{i, j});
                g = new Probability(f);
                s = 0;
                for (int k : g.quantity_ships) {
                    s += k;
                }
            }
//            g.ShowStatusField();
            System.out.println(i);
            sum += c;
        }
        System.out.println(sum / (double) (n));
    }
}