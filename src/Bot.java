public class Bot {
    Field field;
    int dimension;
    int complexity;

    public Bot(Field field, int complexity) {
        dimension = field.dimension;
        this.field = field;
        this.complexity = complexity;
    }

    public void Fire() {
        int[] fire_cell;
        if (complexity == 0) {
            do {
                fire_cell = new int[]{Field.RandomInt(0, dimension - 1), Field.RandomInt(0, dimension - 1)};
            } while (field.position[fire_cell[0]][fire_cell[1]].status < 0);
            field.Fire(fire_cell);
        } else {
            Probability prob = new Probability(field);
            int[] max_pos = prob.GetMaxProbabilityPos(complexity);
            field.Fire(max_pos);
        }
    }
}
