import java.io.IOException;

public class Main {
//    Игра пошаговая, когда я добовлял возможность стрелять несколько раз при попадении сложный бот слишком быстро побеждал
//    и я чувствовал себя несправедливо)
//
//    При большом размере поля и маленьком количестве оставшегося пространства с большой вероятностью
//    корабли не смогут с помощью случайной генерации нормально расположится
    public static void main(String[] args) throws CloneNotSupportedException, IOException {
        new Game();
    }
}