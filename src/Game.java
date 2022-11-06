import java.util.Scanner;
public class Game {
    int dimension;
    Field player_field;
    Field bot_field;
    Probability enemy_status_field;
    int[] quantity_ships;
    Bot bot;

    public Game() {
        Scanner scanner = new Scanner(System.in);;
        int i_pos;
        int j_pos;
        int max_ship_dim;
        int max_amount_ships;
        int complexity;
        do {
            System.out.println("Введите размерность боевого поля: ");
            dimension = scanner.nextInt();
        }while(dimension<2);
        player_field=new Field(dimension);
        bot_field=new Field(dimension);
        do {
            System.out.println("Введите размер палубы самого большого корабля (1-" + dimension + "): ");
             max_ship_dim = scanner.nextInt();
        }while(max_ship_dim>dimension || max_ship_dim<1);
        quantity_ships= new int[max_ship_dim];
        for(int i=max_ship_dim-1;i>=0;--i){
            max_amount_ships = (dimension*dimension-sumShips(quantity_ships))/(i+1);
            do {
                System.out.println("Введите количество кораблей с " + (i + 1) +
                        " палубой (0-" + max_amount_ships + "): ");
                quantity_ships[i] = scanner.nextInt();
            }while(quantity_ships[i]>max_amount_ships || quantity_ships[i]<0 || (i==max_ship_dim-1 && quantity_ships[i]==0));
        }
        player_field.GenerateRandomShips(quantity_ships);
        bot_field.GenerateRandomShips(quantity_ships);
        do {
            System.out.println("Сложность:");
            System.out.println("0- Просто (бот стреляет в случайную клетку на поле)");
            System.out.println("1- Сложно (бот стеляет в клетку c наибольшей математической вероятностью попадания)");
            complexity = scanner.nextInt();
        }while(complexity!=0 && complexity!=1);
        bot= new Bot(player_field, complexity*10000/dimension);
        enemy_status_field=new Probability(bot_field);
        boolean player_is_alive=sumShips(new Probability(player_field).quantity_ships)>0;
        boolean enemy_is_alive=sumShips(enemy_status_field.quantity_ships)>0;
        while(player_is_alive && enemy_is_alive){
            System.out.println("(0) - пустая клетка, (-1) - промах, (-2) Попадение");
            System.out.println("Ваше поле:");
            player_field.ShowField();
            System.out.println("Поле противника:");
            enemy_status_field.ShowStatusField();
            System.out.println("Введите строку по которой будет вестить огонь (1-"+dimension+"):");
            i_pos=scanner.nextInt()-1;
            System.out.println("Введите столбец по которой будет вестись огонь (1-"+dimension+"):");
            j_pos=scanner.nextInt()-1;
            bot_field.Fire(new int[]{i_pos,j_pos});
            bot.Fire();
            enemy_status_field=new Probability(bot_field);
            player_is_alive=sumShips(new Probability(player_field).quantity_ships)>0;
            enemy_is_alive=sumShips(enemy_status_field.quantity_ships)>0;
        }
        System.out.println("Ваше поле:");
        player_field.ShowField();
        System.out.println("Поле противника:");
        enemy_status_field.ShowStatusField();
        System.out.println("---------------------------------");
        System.out.println((!enemy_is_alive?"ИГРОК":"БОТ")+" ПОБЕДИЛ!!!");
        System.out.println("---------------------------------");

    }
    public int sumShips(int[] quantity_ships){
        int sum=0;
        for (int i=0; i<quantity_ships.length;++i){
            sum+=quantity_ships[i]*(i+1);
        }
        return sum;
    }
}
