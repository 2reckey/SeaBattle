public class Field {
    int dimension;
    ShipElement[][] position;
    Ship[] ship_array;

    public Field(int dimension) {
        this.dimension = dimension;
        position = new ShipElement[dimension][dimension];
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                position[i][j] = new ShipElement(new int[]{i, j});
            }
        }
    }

    public void ShowField() {
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                System.out.print("| " + (position[i][j].status >= 0 ? " " : "") + position[i][j].status + "  " + ((j + 1) == dimension ? "|\n" : ""));
            }
        }
        System.out.println();
    }


    public boolean CanAdd(int[] start_pos, int[] end_pos) {
        Boolean cells_are_free = false;
        if ((start_pos[0] < dimension) && (start_pos[1] < dimension)
                && (start_pos[0] >= 0) && (start_pos[1] >= 0)
                && (end_pos[0] < dimension) && (end_pos[1] < dimension)
                && (end_pos[0] >= 0) && (end_pos[1] >= 0)) {
            int change = (start_pos[0] == end_pos[0] ? 1 : 0);
            int ship_dimension = Math.abs(end_pos[change] - start_pos[change]) + 1;
            int sgn = (end_pos[change] - start_pos[change]) / ((ship_dimension - 1 == 0 ? 1 : ship_dimension - 1));
            cells_are_free = true;
            for (int i = 0; i < ship_dimension; ++i) {
                cells_are_free &= (position[start_pos[0] + (change == 0 ? (sgn * i) : 0)]
                        [start_pos[1] + (change == 0 ? 0 : (sgn * i))].status == 0);
            }
        }
        return cells_are_free;
    }

    public int[][] CordsShip(int[] start_pos, int[] end_pos) {
        int change = (start_pos[0] == end_pos[0] ? 1 : 0);
        int ship_dimension = Math.abs(end_pos[change] - start_pos[change]) + 1;
        int sgn = (end_pos[change] - start_pos[change]) / ((ship_dimension - 1 == 0 ? 1 : ship_dimension - 1));
        int[][] ship_pos = new int[ship_dimension][2];
        for (int i = 0; i < ship_dimension; ++i) {
            ship_pos[i] = new int[]{start_pos[0] + (change == 0 ? (sgn * i) : 0), start_pos[1] + (change == 0 ? 0 : (sgn * i))};
        }
        return ship_pos;
    }

    public void AddShip(int[] start_pos, int[] end_pos) {
        if (CanAdd(start_pos, end_pos)) {
            int[][] ship_pos = CordsShip(start_pos, end_pos);
            int ship_dimension = ship_pos.length;
            Ship[] new_ship_array;
            if (ship_array == null) {
                ship_array = new Ship[]{new Ship(this, ship_dimension, ship_pos)};
            } else {
                new_ship_array = new Ship[ship_array.length + 1];
                for (int i = 0; i < ship_array.length; ++i) {
                    new_ship_array[i] = ship_array[i];
                }
                new_ship_array[ship_array.length] = new Ship(this, ship_dimension, ship_pos);
                ship_array = new_ship_array;
            }
        }
    }

    public void Fire(int[] cords) {
        ShipElement cell = position[cords[0]][cords[1]];
        if (cell.status >= 0) {
            cell.status = (cell.belong == null ? -1 : -2);
            if (cell.belong != null) {
                boolean ship_is_alive = false;
                for (ShipElement el : cell.belong.ship_elements) {
                    ship_is_alive |= (el.status == el.belong.dimension);
                }
                if (!ship_is_alive) {
                    for (ShipElement el : cell.belong.ship_elements) {
                        el.belong.status = 0;
                        el.status = -3 * el.belong.dimension;
                    }
                }
            }
        }
    }

    public static int RandomInt(int min, int max) {
        if (max < min) {
            max ^= min;
            min ^= max;
            max ^= min;
        }
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public void GenerateRandomShips(int[] quantity_ships) {
        int[] random_start_cords;
        int[] random_end_cords;
        int random_change;
        for (int i = quantity_ships.length - 1; i >= 0; --i) {
            for (int j = 0; j < quantity_ships[i]; ++j) {
                do {
                    random_start_cords = new int[]{RandomInt(0, dimension - 1), RandomInt(0, dimension - 1)};
                    random_change = RandomInt(0, 1);
                    random_end_cords = new int[]{random_start_cords[0] + (random_change == 0 ? i : 0),
                            random_start_cords[1] + (random_change == 0 ? 0 : i)};
                } while (!CanAdd(random_start_cords, random_end_cords));
                AddShip(random_start_cords, random_end_cords);
            }
        }
    }
}
