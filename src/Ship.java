public class Ship {
    int dimension;
    ShipElement[] ship_elements;
    Field belong;

    int status = 1;

    public Ship(Field belong, int dimension, int[][] ship_pos) {
        this.dimension = dimension;
        this.belong = belong;
        ship_elements = new ShipElement[dimension];
        for (int i = 0; i < dimension; ++i) {
            ship_elements[i] = belong.position[ship_pos[i][0]][ship_pos[i][1]];
            ship_elements[i].belong = this;
            ship_elements[i].status = dimension;
        }
    }
}
