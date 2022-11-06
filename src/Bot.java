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
        Probability prob = new Probability(field);
        int[] max_pos = prob.GetMaxProbabilityPos(complexity);
        field.Fire(max_pos);
    }
}
